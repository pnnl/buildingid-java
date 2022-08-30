# Unique Building Identification (UBID)

**Website:** https://buildingid.pnnl.gov/

## Documentation

### Install

This project is published via [Maven Central](https://search.maven.org/search?q=g:gov.pnnl.buildingid).

Add the following `<dependency>` to your `pom.xml` file:
```xml
<dependency>
  <groupId>gov.pnnl</groupId>
  <artifactId>buildingid</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## Usage

The `buildingid-java` package is a class library.

### The API

* `gov.pnnl.buildingid.Code`
  * Static methods:
    * `gov.pnnl.buildingid.Code encode(double, double, int)`
    * `gov.pnnl.buildingid.Code encode(double, double, double, double, double, double, int)`
  * Instance methods:
    * `gov.pnnl.buildingid.CodeArea decode()`
    * `boolean isValid()`
* `gov.pnnl.buildingid.CodeArea`
  * Instance methods:
    * `gov.pnnl.buildingid.Code encode()`
    * `gov.pnnl.buildingid.CodeArea resize()`

In the following example, a UBID code is decoded and then re-encoded:

```java
package com.example;

import gov.pnnl.buildingid.Code;
import gov.pnnl.buildingid.CodeArea;

class Program {
  public static void main(string[] args) {
    // Initialize UBID code.
    Code code = new Code("849VQJH6+95J-51-58-42-50");

    // Is the UBID code valid?
    if (code.isValid()) {
      try {
        // Decode the UBID code.
        CodeArea codeArea = code.decode();

        // Resize the resulting UBID code area.
        //
        // The effect of this operation is that the height and width of
        // the UBID code area are reduced by half an OLC code area.
        CodeArea newCodeArea = codeArea.resize();

        // Encode the new UBID code area.
        Code newCode = newCodeArea.encode();

        // Test that the new UBID code matches the original.
        System.out.println(code.getValue().equals(newCode.getValue()));
      } catch (IllegalStateException reason) {
        System.out.println(String.format("Call to decode() method failed: %s", reason));
      }
    } else {
      System.out.println("Code is invalid.")
    }
  }
}
```

## License

The code is available as open source under the terms of [The 2-Clause BSD License](https://opensource.org/licenses/BSD-2-Clause).

## Contributions

Contributions are accepted on [GitHub](https://github.com/) via the fork and pull request workflow. See [here](https://help.github.com/articles/using-pull-requests/) for more information.
