(ns newsface.websites
  (:use [newsface persistence youtube] :reload)
  (:require [clojure.contrib.string :as str]
	    [clojure.xml :as xml]
	    [clojure.contrib.logging :as log]
	    [net.cgrand.enlive-html :as en]))


(defn get-website-keywords-from
  "Given the HTML URL of a web page, parses it and returns the keywords
   associated with the page."
  [url]
  (let [parsed-page (en/html-resource (java.net.URL. url))
	keywords (-> (en/select parsed-page [[:meta (en/attr= :name "keywords")]]) 
		     first :attrs :content)]
    {:website url
     :keywords (-> (map str/trim (str/split #"," keywords)) vec)}))


(defn get-websites-tags
  [user-id]
  (let [raw-data (get-links-urls-from user-id)
	urls  (filter (fn [{value :url}]  (not (nil? value))) raw-data)
	sites (filter #(not (is-a-youtube-link? %1)) urls)]
    (filter identity
	    (for [{url :url} sites]
	      (try
		(get-website-keywords-from url)
		(catch Exception e
		  (log/warn (str "Couldn't extract keywords for url: "))))))))