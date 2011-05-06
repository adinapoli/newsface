(ns newsface.core
  (:use compojure.core
	newsface.templates.index
	[hiccup.middleware :only (wrap-base-url)]
	[ring.adapter.jetty] :reload)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response]))


(defroutes *routes*
  (GET "/" [] (index-page))
  (route/resources "/")
  (route/not-found "Page not found"))


(def newsface
  (-> (handler/site *routes*)
      (wrap-base-url)))


(defonce server (run-jetty #'newsface {:port 8080
				       :join? false}))



