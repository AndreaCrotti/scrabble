(ns scrabble.api-test
  (:require [scrabble.api :as sut]
            [ring.mock.request :as mock]
            [clojure.data.json :as json :refer [read-str]]
            [clojure.test :refer [deftest is testing]]))

(deftest test-anagrams
  (testing "getting all the anagrams"
    (let [request (mock/request :get "/api/anagrams" {:word "hello"})
          ;; looks like it's not working during tests but it works fine otherwise
          response (sut/app request)]

      (is (= (:status response) 200))
      (is (= (-> response :body read-str) [])))))

#_(deftest test-best-words
    (testing "get top best words availle"
    ;; not actually correct still here
      (let [request (mock/request :get "/api/best-words" {:tiles "111" :letters "abc"})
            response (sut/app request)]
        (is (= (:status response) 200))
        (is (= (-> response :body read-str) {})))))

;; for some reason the 
#_(let [request (mock/request :get "/api/best-words" {:tiles "111" :letters "abc"})]
    (sut/app request))
