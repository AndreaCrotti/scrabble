(ns scrabble.api
  (:gen-class)
  (:require [compojure
             [core :refer [GET POST defroutes]]
             [route :as route]]
            [clojure.data.json :as json]
            [scrabble.core :as scrabble]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.defaults :refer [api-defaults wrap-defaults]]))

(defn get-words
  "Return all possible words"
  [word]
  (prn "Got request for word " word)
  {:status 200
   :body (json/write-str (scrabble/anagrams word))
   :headers {"Access-Control-Allow-Origin" "*"
             "Access-Control-Allow-Headers" "Content-Type"}})

(defn valued-anagrams [tiles word]
  (let [res (scrabble/valued-anagrams tiles word)]
    {:status 200
     :body (json/write-str res)}))

(defroutes app-routes
  (GET "/" [] "Not defined")
  (GET "/anagrams" [word] (get-words word))
  (GET "/valued-anagrams" [tiles word] (valued-anagrams tiles word))
  (GET "/words" [one] {:status 200 :body "{}"})
  (route/not-found "URL not found"))

(def app
  (wrap-json-response app-routes api-defaults))
#_:access-control-allow-origin [#"http://localhost:3449"]
