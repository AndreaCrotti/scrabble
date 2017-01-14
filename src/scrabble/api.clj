(ns scrabble.api
  (:require [compojure
             [core :refer :all]
             [route :as route]]
            [scrabble.core :as scrabble]
            [ring.middleware.defaults :refer [api-defaults wrap-defaults]]))

(defn get-words [word]
  "Return all possible words"
  {:status 200
   :body (scrabble/possibilities word)}
  )


(defroutes app-routes
  (GET "/" [] "Not defined")
  (GET "/words" [word] get-words)
  (route/not-found "URL not found"))
