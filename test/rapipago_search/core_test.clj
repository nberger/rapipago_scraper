(ns rapipago-search.core-test
  (:import org.apache.http.util.EntityUtils)
  (:require [clojure.test :refer :all]
            [vcr-clj.clj-http :refer [with-cassette] :as vcr]
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
    {:arg-key-fn (fn [req]
                   (-> req
                       (update-in [:body] #(when % (EntityUtils/toString %)))
                       (select-keys (conj vcr/default-req-keys :body))))}

    (let [province (first (provinces/find-by-name "Corrientes"))
          city (->> province cities/find-in-province (filter #(= (:name %) "GOYA")) first)
          search-result (search {:province province :city city})]
      (is (= (count search-result) 4))
      (is (not-any? #{"SABA COMUNICACIONES"} (map :name search-result)))
      (is (some #{"SUPERMERCADO JESUS"} (map :name search-result))))
    (let [province (first (provinces/find-by-name "Cordoba"))
          city (->> province cities/find-in-province (filter #(= (:name %) "ALTA GRACIA")) first)
          search-result (search {:province province :city city})]
      (is (= (count search-result) 2))
      (is (not-any? #{"SUPERMERCADO JESUS"} (map :name search-result)))
      (is (some #{"FARMACIA DE NAPOLI"} (map :name search-result))))))
