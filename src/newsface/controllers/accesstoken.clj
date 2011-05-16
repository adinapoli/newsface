(ns newsface.controllers.accesstoken
  (:use
   [newsface persistence ranking] :reload)
  (:require
   [newsface.templates.accesstoken :as view]))


(defn set-token
  [access-token]
  (do
    (set-access-token access-token)
    (refetch-all-media)
    (view/token-response access-token)))