# lambdaj's closure #

Closures represent probably the most important feature that is missing in the tools box of each Java programmer. Some of them don't feel (or don't understand) the need of closures, while some other does and probably for this reason are evaluating to migrate toward a functional enabled programming language. The biggest part of us just learned to partially workaround this lack by using the verbose and poorly readable (anonymous) inner classes.

lambdaj tries to partially fill this lack by introducing in its release 2.0 a new feature that allow to define, in its traditional DSL style, first-class functions with free variables like in the following example:

```
Closure println = closure(); { of(System.out).println(var(String.class)); }
```

I believe it is straightforward to understand what the println closure does. In particular the var() method binds a free variable of type String to the closure. Moreover, note that the curly brackets around the statement that define the behaviour of the closure are only syntactic sugar and then could be safely removed even if I find the code more readable by keeping them.

Also note that the method invoked on the object returned by the of(...) statement **cannot be a private**. Indeed what the of(object) does is to create a proxy that extends the same class of the object in order to register the invocation on it and bind this invocation to the last created closure. Of course private method are not overridable and then not proxable, so in that case this mechanism doesn't work. If you, by mistake, try to create a closure that invokes a private method, the net effect that you will experience, is that the method itself, not being under the proxy control, get unexpectedly called.

You can then invoke this closure by "closing" its free variable once:

```
println.apply("one");
```

or more times:

```
println.each("one", "two", "three");
```

As you can expect this last statement will cause the Strings "one", "two" and "three" to be printed on 3 different lines of the Java standard output. It is possible to create both untyped (as in the former example) and strongly typed closure. Supposing your classes has a method that sums 2 ints:

```
public int sum(int a, int b) {
    return a + b;
}
```

it is possible to instance a closure that invokes this method as it follows:

```
Closure2<Integer, Integer> adder = closure(Integer.class, Integer.class); { of(this).sum(var(Integer.class), var(Integer.class)); }
```

While you were allowed to call the first closure with any type and number of variables (it will eventually throw an Exception if invoked in a wrong way), you can invoke this second one only by passing 2 ints to it as expected:

```
int result = (Integer)adder.apply(2, 3);
```

## Curry ##

Another feature typically available on closures is the so called curry that allows to create another closure by fixing the value of some free variables. For example you can have a closure of one free argument that adds 10 to any number by doing a curry of the second variable of the former closure as it follows:

```
Closure1<Integer> adderOf10 = adder.curry2(10);
```

In this way by invoking this last closure with the value 3:

```
int result = (Integer)adderOf10.apply(3);
```

you will obtain 13 as expected. In the end, note that you could achieve exactly the same result by directly creating a closure with only one free parameter and the second one already fixed to 10 as in this last statement:

```
Closure1<Integer> adderOf10 = closure(Integer.class, Integer.class); { of(this).sum(var(Integer.class), 10); }
```

## Casting a closure to a one-method interface ##

Sometimes, especially when you have to use some existing API, you could still need to have a closure declared in the standard Java fashion: an interface that declares just a single method. In this case it is possible to cast a closure in that interface, leaving to lambdaj the burden to check if they are compatible. For example, if you need a lambdaj Converter that changes an Integer in another Integer 10 units bigger than the first one, you can cast the former closure as it follows:

```
Converter<Integer, Integer> converterAdding10 = adderOf10.cast(Converter.class);
```

## Why are closures useful? ##

If you still don't see how closures can be useful for you, let me make a slightly more complex example that could show why I think that they are the most powerful tool in order to generalize your code and thus avoid duplications. At this purpose let's write a method that reads a file from the classpath and then prints its content line by line on the Java standard output:

```
public void printFile(String fileName) {
    BufferedReader reader = null;
    try {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);
        reader = new BufferedReader(new InputStreamReader(stream));
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            System.out.println(line); // This is actually the only meaningful statement in this method
        }
    } catch (IOException ioe) {
        throw new RuntimeException("Error while reading file " + fileName, ioe);
    } finally {
        try {
            if (reader != null) reader.close();
        } catch (IOException ioe) {
            throw new RuntimeException("Error while closing file reader", ioe);
        }
    }
}
```

There are lots of bloatware and maybe just one meaningful line of code in this method, isn't it? But now suppose you also need a method that write the file into a String and another one that just counts the number of non-empty lines in the file itself. Instead of copy and paste the former method other two times and change just a single statement in each new method, I think that could be a better idea to generalize it, by passing a closure that tells to the method, case by case, how a line read from the file should be managed, as it follows:

```
public void printFile(String fileName) {
    Closure1<String> lineReader = closure(String.class); { of(System.out).println(var(String.class)); }
    readFileByLine(fileName, lineReader);
}

public String readFile(String fileName) {
    StringWriter sw = new StringWriter();
    Closure1<String> lineReader = closure(String.class); { of(sw).write(var(String.class)); }
    readFileByLine(fileName, lineReader);
    return sw.toString();
}

public int countFileLines(String fileName) {
    lineCounter = 0;
    Closure1<String> lineReader = closure(String.class); { of(this).countNonEmptyLine(var(String.class)); }
    readFileByLine(fileName, lineReader);
    return lineCounter;
}

private int lineCounter = 0;
void countNonEmptyLine(String line) {
    if (line != null && line.trim().length() > 0) lineCounter++;
}
	
private void readFileByLine(String fileName, Closure1<String> lineReader) {
    BufferedReader reader = null;
    try {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);
        reader = new BufferedReader(new InputStreamReader(stream));
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            lineReader.apply(line);
        }
    } catch (IOException ioe) {
        throw new RuntimeException("Error while reading file " + fileName, ioe);
    } finally {
        try {
            if (reader != null) reader.close();
        } catch (IOException ioe) {
            throw new RuntimeException("Error while closing file reader", ioe);
        }
    }
}
```

## Closure's delayed evaluation ##

Lambdaj also provides a different usage pattern called delayed evaluation because the invocation of the closure itself it is delayed until it gets defined. That in turns allows to use closures in a more syntetic and elegant way as in the last example below. Note that here the closure creation has been moved inside the method that uses it, while the corresponding definition comes immediately after the invocation of that method itself.

```
public void printFile(String fileName) {
   readFileByLine(fileName); { of(System.out).println(var(String.class)); }
}

public String readFile(String fileName) {
    StringWriter sw = new StringWriter();
    readFileByLine(fileName); { of(sw).write(var(String.class)); }
    return sw.toString();
}

public int countFileLines(String fileName) {
    lineCounter = 0;
    readFileByLine(fileName); { of(this).countNonEmptyLine(var(String.class)); }
    return lineCounter;
}

private int lineCounter = 0;
void countNonEmptyLine(String line) {
    if (line != null && line.trim().length() > 0) lineCounter++;
}
	
private void readFileByLine(String fileName) {
    delayedClosure(new DelayedClosure<Void>() {

        @Override
        public Void doWithClosure(Closure lineReader) {
            BufferedReader reader = null;
            try {
                InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);
                reader = new BufferedReader(new InputStreamReader(stream));
                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    lineReader.apply(line);
                }
            } catch (IOException ioe) {
                throw new RuntimeException("Error while reading file " + fileName, ioe);
            } finally {
                try {
                    if (reader != null) reader.close();
                } catch (IOException ioe) {
                    throw new RuntimeException("Error while closing file reader", ioe);
                }
            }
            return null;
        }
    });
}
```

This syntax, while already available in the 2.0 release has been vastly improved in the 2.1 one. Moreover it also now allows to return a value from the method that uses the delayed closure as shown in the examples provided in the [lambdaj 2.1 release notes](http://code.google.com/p/lambdaj/wiki/Lambdaj21ReleaseNotes).

## Using the object on which the closure is invoked as unbound variable ##

Starting from lambdaj 2.2 also the object on which the closure is invoked can be used as an unbound variable. In particular by passing to the of() method a class instead of an object you're saying that the object on which the closure will be invoked will be passed during the closure invocation itself. For example if you define a closure as it follows:

```
Closure2<Person, Integer> ageSetter = closure(Person.class, Integer.class); {
    of(Person.class).setAge(var(Integer.class));
}
```

you can then invoke it in this way:

```
ageSetter.apply(person, 35);
```

## Defining a closure without using a ThreadLocal ##

Somebody found confusing (or code smell) to bound a closure to a ThreadLocal before to define it. Starting from the release 2.2, you can create a closure without any ThreadLocal as it follows:

```
Closure2<Person, Integer> ageSetter = new Closure2<Person, Integer>() {{
    of(Person.class).setAge(var(Integer.class));
}};
```

## Defining a closure on a final class or invoking a static method or a constructor ##

By using the method name instead of defining the closure through a dynamic proxy is also possible to let the closure invoking a static method:

```
Closure1<String> intParser = closure(String.class).of(Integer.class, "parseInt", var(String.class));
```

or a method of a final class:

```
Closure2<Integer, Integer> substringOfMyName = closure(Integer.class, Integer.class)
    .of("mario", "substring", var(Integer.class), var(Integer.class));
```

As already stated, the method to be invoked by the closure is defined by its name and not by registering the method invocation on the proxy returned by the of() method. Of course it is possible to define a closure in this way also when the invoked method is not static and doesn't belong to a final class, even if in this way the strong typed invocation is lost.

By using the constant Closure.CONSTRUCTOR as method name in the closure definition form provided by the former point it is possible to define a closure that invokes a constructor. For example if you have a class Person with a constructor Person(String name, int age) you can define a closure that invokes it:

```
Closure2<String, Integer> personCreator = closure()
    .of(Person.class, Closure.CONSTRUCTOR, var(String.class), var(Integer.class));
```

and then create a Person by invoking apply() on this closure as it follows:

```
Person me = (Person)personCreator.apply("Mario", 36);
```