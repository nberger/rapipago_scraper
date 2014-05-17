(ns rapipago-search.core-test
  (:require [clojure.test :refer :all]
            [vcr-clj.clj-http :refer [with-cassette]]
            [rapipago-search.provinces :as provinces]
            [rapipago-search.cities :as cities]
            [rapipago-search.core :refer :all]))

(deftest search-without-filter
  (with-cassette :search-without-filter
    (let [search-result (take 20 (search {}))]
      (is (= (count search-result) 20))
      (is (some #{"LOCUTORIOS INTERVISION"} (map :name search-result))))))

(deftest search-with-province-filter
  (with-cassette :search-with-province-filter
    (let [province (first (provinces/find-by-name "Corrientes"))
          search-result (take 20 (search {:province province}))]
      (is (= (count search-result) 20))
      (is (not-any? #{"LOCUTORIOS INTERVISION"} (map :name search-result)))
      ; checks result from 1st page
      (is (some #{"SABA COMUNICACIONES"} (map :name search-result)))
      ; checks result from 3rd page
      (is (some #{"AYALA ALICIA SUSAN"} (map :name search-result))))))

(deftest search-with-city-filter
  (with-cassette :search-with-city-filter
    (let [province (first (provinces/find-by-name "Corrientes"))
          city (->> province cities/find-in-province (filter #(= (:name %) "GOYA")) first)
          search-result (search {:province province :city city})]
      (is (= (count search-result) 4))
      (is (not-any? #{"SABA COMUNICACIONES"} (map :name search-result)))
      (is (some #{"SUPERMERCADO JESUS"} (map :name search-result))))))
