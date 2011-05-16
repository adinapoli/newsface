(ns newsface.core
  (:use compojure.core
	[newsface.templates
	 index contacts accesstoken news suggest
	 notfound stats]
	[newsface.controllers accesstoken]
	[hiccup.middleware :only (wrap-base-url)]
	[ring.adapter.jetty] :reload)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response]))


(defroutes *routes*
  (GET  "/" [] (index-page))
  (GET  "/contacts/" [] (contacts-page))
  (GET  "/accesstoken/" [] (accesstoken-page))
  (GET  "/suggest/" [] (suggest-page))
  (GET  "/news/" [] (news-page))
  (GET  "/stats/" [] (stats-page))
  (POST "/settoken/" {params :params} (set-token (str (get params :access-token)))) 
  (route/resources "/")
  (route/not-found (not-found-page)))

;;(POST "/settoken/" [access-token] (set-access-token access-token))

(def newsface
  (-> (handler/site *routes*)
      (wrap-base-url)))


;;(defonce server (run-jetty #'newsface {:port 8080
;;:join? false}))



