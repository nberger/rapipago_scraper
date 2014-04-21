(ns rapipago-search.core-test
  (:require [clojure.test :refer :all]
            [vcr-clj.clj-http :refer [with-cassette]]
            [rapipago-search.provinces :as provinces]
            [rapipago-search.cities :as cities]
            [rapipago-search.core :refer :all]))

(deftest search-without-filter
  (with-cassette :search-without-filter
    (let [search-result (search {})]
      (is (= (count search-result) 6))
      (is (some #{"LOCUTORIOS INTERVISION"} (map :name search-result))))))

(deftest search-with-province-filter
  (with-cassette :search-with-province-filter
    (let [province (first (provinces/find-by-name "Corrientes"))
          search-result (search {:province province})]
      (is (= (count search-result) 6))
      (is (not-any? #{"LOCUTORIOS INTERVISION"} (map :name search-result)))
      (is (some #{"SABA COMUNICACIONES"} (map :name search-result))))))

(deftest search-with-city-filter
  (with-cassette :search-with-city-filter
    (let [province (first (provinces/find-by-name "Corrientes"))
          city (->> province cities/find-in-province (filter #(= (:name %) "GOYA")) first)
          search-result (search {:province province :city city})]
      (is (= (count search-result) 3))
      (is (not-any? #{"SABA COMUNICACIONES"} (map :name search-result)))
      (is (some #{"SUPERMERCADO JESUS"} (map :name search-result))))))
