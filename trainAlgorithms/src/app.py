import joblib
import pandas as pd
import numpy as np
import psycopg2
from flask import Flask, jsonify
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_squared_error, root_mean_squared_error
from sklearn.model_selection import GridSearchCV
from sklearn.model_selection import train_test_split
from sklearn.neural_network import MLPRegressor
from sklearn.preprocessing import StandardScaler
from sklearn.tree import DecisionTreeRegressor

from src.predictResponse import Prediction
from src.predictionValue import PredictionValue

app = Flask(__name__)


def mean_absolute_percentage_error(y_true, y_pred):
    return np.mean(np.abs((y_true - y_pred) / (y_true + y_pred) / 2)) * 100


@app.route("/weather/train", methods=['GET'])
def index():
    prediction_dt = train_with_decision_tree()
    prediction_rf = train_with_random_forest()
    prediction_mlp = train_with_mlp()
    predictions = [prediction_dt, prediction_rf, prediction_mlp]
    predictions_dict_list = [prediction._asdict() for prediction in predictions]

    return jsonify(predictions_dict_list)


@app.route("/predict/start/<start>/end/<end>/deviceNumber/<deviceNumber>", methods=['GET'])
def predict_on_period(start, end, deviceNumber):
    data = getWeatherDatas()

    data['date'] = pd.to_datetime(data['date'], format='%d/%m/%Y %H:%M')
    data['Month'] = data['date'].dt.month
    data['Hour'] = data['date'].dt.hour

    start_date = pd.to_datetime(start, format='%d-%m-%Y %H:%M')
    previous_start_date = start_date - pd.Timedelta(days=1)
    end_date = pd.to_datetime(end, format='%d-%m-%Y %H:%M')
    next_end_date = end_date + pd.Timedelta(days=1)

    filtered_df = data[(data['date'] >= previous_start_date) & (data['date'] < next_end_date)]
    copy_data = pd.to_datetime(filtered_df['date'], format='%d-%m-%Y %H:%M').dt.strftime('%d-%m-%Y')
    copy_filtered_df = filtered_df

    for i in range(len(copy_filtered_df['real_value'])):
        if i in filtered_df.index and i in copy_filtered_df.index:
            filtered_df.loc[i, 'real_value'] = copy_filtered_df.loc[i, 'real_value'] / 16
            filtered_df.loc[i, 'real_value'] = filtered_df.loc[i, 'real_value'] * np.float64(deviceNumber)

    X = filtered_df.drop(columns=['real_value'])
    y = filtered_df['real_value']

    X['date'] = X['date'].values.astype("int64")

    dt_model = joblib.load('decision_tree_model.pkl')
    y_pred_dt = dt_model.predict(X)

    rmse_dt = root_mean_squared_error(y, y_pred_dt)
    mape_dt = mean_absolute_percentage_error(y, y_pred_dt)
    mse_dt = mean_squared_error(y, y_pred_dt)

    rf_model = joblib.load('random_forest_model.pkl')
    y_pred_rf = rf_model.predict(X)

    rmse_rf = root_mean_squared_error(y, y_pred_rf)
    mape_rf = mean_absolute_percentage_error(y, y_pred_rf)
    mse_rf = mean_squared_error(y, y_pred_rf)

    X['date'] = X['date'].values.astype("int64")

    scaler = StandardScaler()
    scaled_data = scaler.fit_transform(X)

    mlp_model = joblib.load('mlp_model.pkl')
    y_pred_mlp = mlp_model.predict(scaled_data)

    rmse_mlp = root_mean_squared_error(y, y_pred_mlp)
    mape_mlp = mean_absolute_percentage_error(y, y_pred_mlp)
    mse_mlp = mean_squared_error(y, y_pred_mlp)

    prediction_dt = PredictionValue(date=copy_data.tolist(), dt=y_pred_dt.tolist(), rf=y_pred_rf.tolist(),
                                    mlp=y_pred_mlp.tolist(),
                                    real=y.tolist(),
                                    rmse_dt=np.append([], rmse_dt).tolist(),
                                    mape_dt=np.append([], mape_dt).tolist(),
                                    mse_dt=np.append([], mse_dt).tolist(),
                                    rmse_rf=np.append([], rmse_rf).tolist(),
                                    mape_rf=np.append([], mape_rf).tolist(),
                                    mse_rf=np.append([], mse_rf).tolist(),
                                    rmse_mlp=np.append([], rmse_mlp).tolist(),
                                    mape_mlp=np.append([], mape_mlp).tolist(),
                                    mse_mlp=np.append([], mse_mlp).tolist())

    results = [prediction_dt]
    predictions_dict_list = [result._asdict() for result in results]

    return jsonify(predictions_dict_list)


def split_data():
    data = getWeatherDatas()

    data['date'] = pd.to_datetime(data['date'], dayfirst=True)
    data['Month'] = data['date'].dt.month
    data['Hour'] = data['date'].dt.hour

    X = data.drop(columns=['real_value'])
    y = data['real_value']

    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

    X_train['date'] = X_train['date'].values.astype("int64")
    X_test['date'] = X_test['date'].values.astype("int64")

    return X_train, X_test, y_train, y_test


def train_with_decision_tree():
    X_train, X_test, y_train, y_test = split_data()
    custom_conditions = [
        (X_train['Month'].isin([10, 11, 12, 1, 2])) & ((X_train['Hour'] >= 17) | (X_train['Hour'] < 7))]

    # Decision Tree Regressor

    model = DecisionTreeRegressor(random_state=42)
    model.fit(X_train, y_train, sample_weight=(~custom_conditions[0]).astype(float))
    joblib.dump(model, 'decision_tree_model.pkl')

    y_pred_dt = model.predict(X_test)

    rmse_dt = (root_mean_squared_error(y_test, y_pred_dt))
    print('RMSE Decision Tree Regressor:', rmse_dt)
    mse_dt = (mean_squared_error(y_test, y_pred_dt))
    print('MSE Decision Tree Regressor:', mse_dt)
    mape_dt = mean_absolute_percentage_error(y_test, y_pred_dt)
    print('MAPE Decision Tree Regressor:', mape_dt)

    X_test['date'] = pd.to_datetime(X_test['date'], dayfirst=True).astype('int64') // 10 ** 9

    return Prediction(RMSE=np.float64(rmse_dt), MSE=np.float64(mse_dt), MAPE=np.float64(mape_dt))


def train_with_random_forest():
    X_train, X_test, y_train, y_test = split_data()
    custom_conditions = [
        (X_train['Month'].isin([10, 11, 12, 1, 2])) & ((X_train['Hour'] >= 17) | (X_train['Hour'] < 7))]

    X_train_filtered = X_train[~custom_conditions[0]]
    y_train_filtered = y_train[~custom_conditions[0]]

    # Random Forest

    rf_regressor = RandomForestRegressor(n_estimators=95, random_state=42)
    rf_regressor.fit(X_train_filtered, y_train_filtered)
    joblib.dump(rf_regressor, 'random_forest_model.pkl')
    y_pred_rf = rf_regressor.predict(X_test)

    rmse_rf = (root_mean_squared_error(y_test, y_pred_rf))
    print('RMSE Random Forest:', rmse_rf)
    mse_rf = (mean_squared_error(y_test, y_pred_rf))
    print('MSE Random Forest:', mse_rf)
    mape_rf = mean_absolute_percentage_error(y_test, y_pred_rf)
    print('MAPE Random Forest:', mape_rf)

    return Prediction(RMSE=np.float64(rmse_rf), MSE=np.float64(mse_rf), MAPE=np.float64(mape_rf))


def train_with_mlp():
    X_train, X_test, y_train, y_test = split_data()

    model = MLPRegressor(activation='relu', random_state=42)

    param_grid = {
        'hidden_layer_sizes': [(50,), (100,), (50, 50), (100, 50), (100, 100)],
        'alpha': [0.0001, 0.001, 0.01, 0.1],
        'learning_rate_init': [0.001, 0.01, 0.1],
    }

    scaler = StandardScaler()
    scaled_data = scaler.fit_transform(X_train)
    scaled_X_test = scaler.transform(X_test)

    grid_search = GridSearchCV(model, param_grid, cv=5, scoring='neg_mean_squared_error')
    grid_search.fit(scaled_data, y_train)

    best_mlp = grid_search.best_estimator_
    joblib.dump(best_mlp, 'mlp_model.pkl')
    y_pred = best_mlp.predict(scaled_X_test)

    rmse_mlp = (root_mean_squared_error(y_test, y_pred))
    print("RMSE:", rmse_mlp)
    mape_mlp = mean_absolute_percentage_error(y_test, y_pred)
    print("MAPE:", mape_mlp)
    mse_mlp = (mean_squared_error(y_test, y_pred))
    print("MSE: ", mse_mlp)

    return Prediction(RMSE=np.float64(rmse_mlp), MSE=np.float64(mse_mlp), MAPE=np.float64(mape_mlp))


def getWeatherDatas():
    conn = psycopg2.connect(database="mydb", user="postgres",
                            password="0000", host="localhost", port="5432")
    cur = conn.cursor()
    cur.execute('SELECT * FROM weathermanagement.weather_data')
    datas = cur.fetchall()
    columns = [desc[0] for desc in cur.description]
    dataFrame = pd.DataFrame(datas, columns=columns)
    cur.close()
    conn.close()

    return dataFrame


if __name__ == "__main__":
    from waitress import serve

    serve(app, host="0.0.0.0", port=5000)
