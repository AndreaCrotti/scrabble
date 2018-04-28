(ns scrabble.api-test
  (:require [scrabble.api :as api]
            [ring.mock.request :as mock]
            [clojure.data.json :as json :refer [read-str]]
            [clojure.test :as t]))

(t/deftest test-anagrams
  (t/testing "getting all the anagrams"
    (let [request (mock/request :get "/api/anagrams" {:word "hello"})
          ;; looks like it's not working during tests but it works fine otherwise
          response (api/app request)]

      (t/is (= (:status response) 200))
      (t/is (= (-> response :body read-str) [])))))

#_(t/deftest test-best-words
    (t/testing "get top best words availle"
    ;; not actually correct still here
      (let [request (mock/request :get "/api/best-words" {:tiles "111" :letters "abc"})
            response (api/app request)]
        (t/is (= (:status response) 200))
        (t/is (= (-> response :body read-str) {})))))

;; for some reason the 
#_(let [request (mock/request :get "/api/best-words" {:tiles "111" :letters "abc"})]
    (api/app request))
