# Simple Example Dremio Internal Functions

[![Build Status](https://travis-ci.org/dremio-hub/dremio-internal-function-example.svg?branch=master)](https://travis-ci.org/dremio-hub/dremio-internal-function-example)

This shows an example a custom function using Dremio's internal APIs. 


## Sample Functions
There are 3 examples in this repository, 'addstuff', 'concat' and 'protect' which are described in the following sections.

### AddStuff Example
* Purpose:
   * Demonstrate how to handle numbers as UDF function parameters
   
* Usage example
   ```SELECT addstuff(5,10) from (values 1)```
   
   
### ConcatOp Example
* Purpose:
   * Demonstrate how to handle numbers as UDF function parameters
   
* Usage example
   ```SELECT example_concat_op("First","Second") from (values 1)```
   
## Protect Example
* Purpose:
   * Setup a Protegrity sample example to do the protect operation. [NOTE: not certain whether this really is the way to call protegrity]
   
* Usage example:
    ```SELECT protect("Category",'ABC') from sf_incidents```
    
    This example assumes that you are using the samples directory and the sf_incidents file.
    Column names have "{Column name}" and the token text value is setup using '{protegrity token name}'
       
## UnProtect Example
* Purpose:
   * Setup a Protegrity sample example to do the unprotect operation. [NOTE: not certain whether this really is the way to call protegrity]
   
* Usage example:
    ```SELECT unprotect("Category",'ABC') from sf_incidents```
    
    This example assumes that you are using the samples directory and the sf_incidents file.
    Column names have "{Column name}" and the token text value is setup using '{protegrity token name}'       
   
## General comments
* @params
    Defines the UDF function parameters
    
* @output
    Defines the buffer which will be placed in the SELECT statement output
    
* @Workspace
    Defines temporary variables for use within the UDF
* setup()
    Executed at the start of the query.  Used to grab variables useful as a constant for all rows in the query.  
    
* eval()
    Executed on each row of the query.   The input values originate from the @param values and the output is an ArrowBuf identified by the @output attribute.
    
     

## To Build and deploy
mvn clean package
cp {target dir} <Dremio Home>/Java/dremio/jars/3rdparty
restart dremio



