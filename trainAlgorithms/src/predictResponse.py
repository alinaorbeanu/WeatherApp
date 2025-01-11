from typing import NamedTuple

from numpy import float64


class Prediction(NamedTuple):
    RMSE: float64
    MSE: float64
    MAPE: float64
