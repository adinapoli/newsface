(ns newsface.controllers.train
  (:use [newsface persistence ranking] :reload)
  (:require
   [newsface.templates.train :as train-view]))



(defn train-request
  []
  (if (get @facebook-auth :access-token) 
    (do
      (train-from-scratch)
      (train-view/train-response "Train successfully completed."))
    (train-view/train-response "You should provide a token in order to start the training.")))