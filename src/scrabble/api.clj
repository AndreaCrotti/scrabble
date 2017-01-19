(ns scrabble.api
  (:gen-class)
  (:require [compojure
             [core :refer :all]
             [route :as route]]
            [clojure.data.json :as json]
            [scrabble.core :as scrabble]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.defaults :refer [api-defaults wrap-defaults]]))


(defn get-words [word]
  "Return all possible words"
  {:status 200
   :body (json/write-str (scrabble/anagrams word))}
  )


(defroutes app-routes
  (GET "/" [] "Not defined")
  (GET "/words" [word] (get-words word))
  (GET "/anagrams" [word] (get-words word))
  (route/not-found "URL not found"))

(def app
  (wrap-json-response app-routes api-defaults))
