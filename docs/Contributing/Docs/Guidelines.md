You need to follow these rules in order to contribute to the docs. They are important for a good user experience and provide
a consistent baseline for other contributors to work with.
##Links

###Internal

Links to internal pages can be opened in the same tab. This works with the normal markdown link syntax:

``` linenums="1"
Click the [highlighted words](Contributing.md).
```
Result: Click the [highlighted words](../Contributing.md).

###External
Links to external sites must be opened in new tabs using this html code:

``` HTML linenums="1"
<a href="https://betonquest.pl/" target="_blank">Clickable text that opens a new tab</a>
```
Result: <a href="https://betonquest.pl/" target="_blank">Clickable text that opens a new tab</a>



##Displaying (YAML) code

``` linenums="1"
 ``` YAML linenums="1"
 use: "codeboxes for code"
 ```
```

Result:
``` YAML linenums="1"
use: "codeboxes for code"
```

##File names

Replace all spaces in file and folder names with `-`!

##Lists

Lists must be declared as such:

```
* Top Level
    - Second Level
    - Second Level
* Another Top level
```

Result:

* Top Level
    - Second Level
    - Second Level
* Another Top level

##Line length
A single line may only have 170 characters. Please wrap at 121 though.