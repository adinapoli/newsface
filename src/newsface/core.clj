(ns newsface.core
  (:use compojure.core
	[newsface.templates
	 index contacts accesstoken news suggest
	 notfound stats]
	[hiccup.middleware :only (wrap-base-url)]
	[ring.adapter.jetty] :reload)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response]))


(defroutes *routes*
  (GET "/" [] (index-page))
  (GET "/contacts/" [] (contacts-page))
  (GET "/accesstoken/" [] (accesstoken-page))
  (GET "/suggest/" [] (suggest-page))
  (GET "/news/" [] (news-page))
  (GET "/stats/" [] (stats-page))
  (route/resources "/")
  (route/not-found (not-found-page)))


(def newsface
  (-> (handler/site *routes*)
      (wrap-base-url)))


;;(defonce server (run-jetty #'newsface {:port 8080
;;:join? false}))



