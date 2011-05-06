;;Auth key: AI39si6x3yWFQjZkkPZT_gEbr8OzTzAmr4G94J_zhRmAfU9AHWxaY1Gxhq7nLmeSALcLZlpM0uBRBsM6nw3LmpbU8KMNA0sFOQ
(ns newsface.youtube
  (:use [newsface persistence] :reload)
  (:require [clojure.contrib.string :as str]
	    [clojure.xml :as xml]
	    [clojure.contrib.logging :as log]
	    [net.cgrand.enlive-html :as en]))


(defn get-video-id
  "Given a valid youtube video link, returns the video id."
  [video-link]
  ;;Since I use a split by = or & the video-id will be between
  ;;the standard url (e.g. watch?v=) and the first get params (e.g &bla bla).
  (nth (str/split #"=|&" video-link) 1))


(defn get-video-keywords-from
  "Given a video-id, extract the keyword associated to the video."
  [video-id]
  (let [url (str  "http://gdata.youtube.com/feeds/api/videos/" video-id)
	parsed-page (en/html-resource (java.net.URL. url))
	keywords (-> (en/select parsed-page [[:media:keywords]]) 
		     first :content first)]
    {:video video-id
     :keywords (-> (map str/trim (str/split #"," keywords)) vec)}))


(defn is-a-youtube-link?
  [{url :url}]
  (when-not (nil? url)
    (let [url-tokens (re-seq #"\w+" url)]
      (not (empty? (str/grep #"youtube" url-tokens))))))


(defn get-videos-tags
  [user-id]
  (let [raw-data (get-links-urls-from user-id)
	urls (filter (fn [{value :url}]  (not (nil? value))) raw-data)
	videos (filter is-a-youtube-link? urls)]
    (for [{url :url} videos]
      (try
	(get-video-keywords-from (get-video-id url))
	(catch Exception e
	  (log/error (str "Couldn't retrieve tag from: " url)))))))


