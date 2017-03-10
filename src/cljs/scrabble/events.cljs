(ns scrabble.events
  (:require [ajax.core :refer [GET]]
            [re-frame.core :as re-frame :refer [dispatch reg-event-db]]
            [scrabble.db :as db]
            [scrabble.query :as query :refer [find-matches]]))


(reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(reg-event-db
 :set-language
 (fn [db [_ language]]
   (assoc db :language language)))

;; the number of points will depend automatically on the language that was currently chosen

;; these two are really the same thing in a way
(reg-event-db
 :set-tile
 (fn [db [_ idx tile]]
   (assoc-in db [:tiles idx] tile)))

(reg-event-db
 :set-letter
 (fn [db [_ idx tile]]
   (assoc-in db [:letters idx] tile)))

(reg-event-db
 :get-results
 (fn [db _]
   ;; use juxt here instead?
   (assoc db :results (find-matches (:language db) (:tiles db) (:letters db)))))

(reg-event-db
 :clear-anagrams
 (fn [db _]
   (-> db
       (assoc :fetching? false)
       (assoc :word-to-anagram nil)
       (assoc :anagrams []))))

(reg-event-db
 :clear-best-word
 (fn [db _]
   (-> db
       (assoc :letters {})
       (assoc :tiles {}))))

(reg-event-db
 :fetch-anagrams
 (fn [db _]
   ;; TODO change it to use the secondo form with reg-event-fx instead
   (GET "http://localhost:3000/anagrams"
        {:params {:word (:word-to-anagram db)}
         :handler #(dispatch [:set-anagrams %1])
         :error-handler #(prn "got response error" %1)})
   
   (assoc db :fetching? true)))

(reg-event-db
 :set-anagrams
 (fn [db [_ response]]
   (prn response)
   (-> db
       (assoc :fetching? false)
       (assoc :anagrams (js->clj response)))))

(reg-event-db
 :set-word-to-anagram
 (fn [db [_ word-to-anagram]]
   (assoc db :word-to-anagram word-to-anagram)))
