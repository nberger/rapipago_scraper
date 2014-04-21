(ns rapipago-search.core
  (require [clojure.string :as string]
           [net.cgrand.enlive-html :as html]
           [clj-http.client :as http]
           [rapipago-search.provinces :as provinces]
           [rapipago-search.cities :as cities]))

(defn- fetch-search-result
  [{province :province city :city}]
  (let [url "http://www.rapipago.com.ar/rapipagoWeb/suc-buscar.htm"
        post-params {:provinciaSuc (:id province)
                     :ciudadSuc (:id city)}]
    (:body (http/post url
                      {:form-params post-params}))))

(comment
  (def province (second (provinces/find-all)))
  (def city (first (cities/find-in-province province)))
  (fetch-search-result {:province province :city city})
  )

(defn- build-rapipago
  [id-tag name-tag address-tag]
  (let [id (first (:content id-tag))
        name (first (:content name-tag))
        address (first (:content address-tag))]
  {:id id
   :name (string/trim name)
   :address address}))

(defn- parse-rapipagos
  [rapipagos-html-result]
  (let [html-result (html/html-snippet rapipagos-html-result)
        ids (html/select html-result [:.resultadosNumeroSuc])
        names (html/select html-result [:.resultadosTextWhite])
        resultadosText (html/select html-result [:.resultadosText])
        addresses (keep-indexed #(if (even? %1) %2) resultadosText)]
    (map build-rapipago ids names addresses)))

(comment
  (def result (fetch-search-result {:province province :city city}))
  (parse-rapipagos result)
  )

(defn search
  [{province :province city :city :as filter :or {:province {} :city {}}}]
  (let [result (fetch-search-result filter)
        rapipagos (parse-rapipagos result)]
    (map #(merge filter %1) rapipagos)))

(comment
        (search {:province province :city city})

        (search {})
  )
