# Clj-kondo config

This repo contains configurations and/or recommendations that can be used with
[clj-kondo](https://github.com/borkdude/clj-kondo/). Check the `resources` folder for available configs.

## Installation

This repo has an **experimental** main entrypoint that copies library specific config and hooks to your `.clj-kondo`.

Assuming the following alias in your `deps.edn`:

``` clojure
:clj-kondo/config
{:extra-deps {clj-kondo/config {:git/url "https://github.com/clj-kondo/config"
                                :sha "<latest-sha>"}}
 :main-opts ["-m" "clj-kondo.config"]}
```

you can invoke it with:

``` shell
$ clojure -A:clj-kondo/config --lib rum --lib slingshot
```

This then copies Rum and Slingshot config into `.clj-kondo/configs/rum` and `.clj-kondo/configs/slingshot` respectively:

``` shell
$ clojure -A:clj-kondo/config --lib rum --lib slingshot
Copying rum config to .clj-kondo/configs/rum
Copying slingshot config to .clj-kondo/configs/slingshot
Add "configs/rum", "configs/slingshot" to :config-paths in .clj-kondo/config.edn to activate configs.
```

To activate, you then add `"configs/rum"` and `"configs/slingshot"`  to your `:config-paths` in `config.edn` and Rum and Slinghot syntax will be recognized.

## License

Copyright © 2020 Michiel Borkent

Distributed under the EPL License, same as Clojure. See LICENSE.
