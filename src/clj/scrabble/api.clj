(ns scrabble.api
  (:gen-class)
  (:require [compojure
             [core :refer [GET POST defroutes]]
             [route :as route]]
            [clojure.data.json :as json]
            [scrabble.core :as scrabble]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.defaults :refer [api-defaults wrap-defaults]]))

(defn get-words
  "Return all possible words"
  [word]
  (prn "Got request for word " word)
  {:status 200
   :body (json/write-str (scrabble/anagrams word))
   ;; TODO: implement some more proper security here instead
   ;; with the right settings on deployment
   :headers {"Access-Control-Allow-Origin" "*"
             "Access-Control-Allow-Headers" "Content-Type"}})

(defn best-words [tiles word]
  (let [res (scrabble/best-words tiles word)]
    {:status 200
     :body (json/write-str res)}))

(defroutes app-routes
  (GET "/" [] "Not defined")
  (GET "/anagrams" [word] (get-words word))
  (GET "/best-words" [tiles word] (best-words tiles word))
  (GET "/words" [one] {:status 200 :body "{}"})
  (route/not-found "URL not found"))

(def app
  (wrap-json-response app-routes api-defaults))

