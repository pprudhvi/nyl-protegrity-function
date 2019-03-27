# Simple Example Dremio Internal Functions

[![Build Status](https://travis-ci.org/dremio-hub/dremio-internal-function-example.svg?branch=master)](https://travis-ci.org/dremio-hub/dremio-internal-function-example)

This shows an example a custom function using Dremio's internal APIs. 


## Sample Functions
There are 3 examples in this repository, 'addstuff', 'concat' and 'protect' which are described in the following sections.

## To Build and deploy
mvn clean package
cp {target dir} <Dremio Home>/Java/dremio/jars/3rdparty
restart dremio



