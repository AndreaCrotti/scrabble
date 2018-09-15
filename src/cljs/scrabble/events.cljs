(ns scrabble.events
  (:require [ajax.core :refer [GET]]
            [day8.re-frame.http-fx]
            [re-frame.core :as rf]
            [scrabble.db :as db]
            [scrabble.query :as query :refer [find-matches]]))


(rf/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(rf/reg-event-db
 :set-language
 (fn [db [_ language]]
   (assoc db :language language)))

;; the number of points will depend automatically on the language that was currently chosen

;; these two are really the same thing in a way
(rf/reg-event-db
 :set-tile
 (fn [db [_ idx tile]]
   (assoc-in db [:tiles idx] tile)))

(rf/reg-event-db
 :set-letter
 (fn [db [_ idx tile]]
   (assoc-in db [:letters idx] tile)))

(rf/reg-event-db
 :clear-anagrams
 (fn [db _]
   (-> db
       (assoc :fetching? false)
       (assoc :word-to-anagram nil)
       (assoc :anagrams []))))

(rf/reg-event-db
 :clear-best-word
 (fn [db _]
   (-> db
       (assoc :results {})
       (assoc :letters {})
       (assoc :tiles {}))))

(defn- concat-map-letters [letters-map]
  ;; add some checks to make sure the size is actually matchin
  (let [sorted-keys (sort (keys letters-map))]
    (clojure.string/join
     ""
     (for [idx sorted-keys]
       (get letters-map idx)))))

(rf/reg-event-db
 :set-best-words
 (fn [db [_ best-words]]
   (-> db
       (assoc :fetching? false)
       (assoc :results (js->clj best-words)))))

(rf/reg-event-db
 :get-results
 (fn [db _]
   (let [letters (concat-map-letters (:letters db))
         tiles (concat-map-letters (:tiles db))]

     (GET "http://localhost:3000/api/best-words"
          {:params {:letters letters :tiles tiles}
           :handler #(rf/dispatch [:set-best-words %1])
           :error-handler #(prn "got error response" %1)}))
   
   (assoc db :fetching? true)))

(rf/reg-event-db
 :fetch-anagrams
 ;;TODO: replace it using the right library
 (fn [db _]
   ;; TODO change it to use the secondo form with reg-event-fx instead
   (GET "http://localhost:3000/api/anagrams"
        {:params {:word (:word-to-anagram db)}
         :handler #(rf/dispatch [:set-anagrams %1])
         :error-handler #(prn "got response error" %1)})
   
   (assoc db :fetching? true)))

(rf/reg-event-db
 :set-anagrams
 (fn [db [_ response]]
   (-> db
       (assoc :fetching? false)
       (assoc :anagrams (js->clj response)))))

(rf/reg-event-db
 :set-word-to-anagram
 (fn [db [_ word-to-anagram]]
   (assoc db :word-to-anagram word-to-anagram)))
