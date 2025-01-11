from typing import NamedTuple, List

import numpy as np
from numpy import float64


class PredictionValue(NamedTuple):
    date: List[str]
    dt: List[float64]
    rf: List[float64]
    mlp: List[float64]
    real: List[float64]
    rmse_dt: List[float64]
    mape_dt: List[float64]
    mse_dt: List[float64]
    rmse_rf: List[float64]
    mape_rf: List[float64]
    mse_rf: List[float64]
    rmse_mlp: List[float64]
    mape_mlp: List[float64]
    mse_mlp: List[float64]
