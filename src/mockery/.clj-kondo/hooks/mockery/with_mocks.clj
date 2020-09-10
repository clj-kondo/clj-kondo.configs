(ns hooks.mockery.with-mocks
  (:require [clj-kondo.hooks-api :as api]))


(defn with-mocks [{:keys [:node]}]
  (let [[binding-vec & body] (rest (:children node))
        bindings (:children binding-vec)]
    (try
      (when-not binding-vec
        (throw (ex-info "No mocks provided" (meta node))))
      (when-not (vector? (api/sexpr binding-vec))
        (throw (ex-info "with-mocks requires vector for its bindings" (meta binding-vec))))
      (when (zero? (count (api/sexpr binding-vec)))
        (api/reg-finding! (assoc (meta (first (:children node)))
                                 :message "with-mocks with no mocks"
                                 :type :mockery)))
      (when-not (and binding-vec
                     (even? (count (api/sexpr binding-vec))))
        (throw (ex-info "Mock vector requires an even number of forms"
                        (meta binding-vec))))
      (when-let [mock (first (drop-while #(symbol? (api/sexpr %))
                                         (take-nth 2 (:children binding-vec))))]
        (throw (ex-info "Mock is not a symbol" (meta mock))))
      (when-let [value (first (drop-while #(map? (api/sexpr %))
                                          (take-nth 2 (drop 1 (:children binding-vec)))))]
        (throw (ex-info "Mock binding is not a map" (meta value))))
      (doseq [m (take-nth 2 (drop 1 (api/sexpr binding-vec)))]
        (if-let [target (:target m)]
          (when-not (qualified-keyword? target)
            (throw (ex-info (str target " must be fully qualified") (meta binding-vec))))
          (throw (ex-info "no target specified" (meta binding-vec)))))
      (when-not body
        (api/reg-finding! (assoc (meta (first (:children node)))
                                 :message "with-mocks with empty body"
                                 :type :mockery)))
      {:node (api/list-node
              (list*
               (api/token-node 'let*)
               (api/vector-node bindings)
               `(~(into [] (take-nth 2 bindings))
                 ~@body)))}
      (catch Exception e
        (api/reg-finding! (assoc (ex-data e) :message (ex-message e) :type :hook))))))
