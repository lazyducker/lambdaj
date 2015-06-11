#Describes build and release project with our configured maven pom

# Building #

Building is done with normal maven usage:

mvn clean install

# Release with Dependencies #

To build a jar that includes all maven assemblies, call:

mvn assembly:assembly

# Release to public Maven Repository #

Lambdaj is available in the maven public repository, so in order to use it in your maven project it is enough to add the following dependency to your pom file:

```
<dependency>
    <groupId>com.googlecode.lambdaj</groupId>
    <artifactId>lambdaj</artifactId>
    <version>2.3.1</version>
</dependency>
```

# Lambdaj Repository #

You can also download lambdaj directly from the project repository by adding it to your pom file as it follows:

```
<repositories>
    <repository>
        <id>lambdaj</id>
        <name>Lambdaj Repository</name>
        <url>http://lambdaj.googlecode.com/svn/repo/releases/</url>
    </repository>
</repositories>
```