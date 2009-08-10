The best way to understand what lambdaj does and how it works is to start asking why we felt the need to develop it.

We were working on a project that had a very complex data model and we found our code was full of repetitions and tons of pieces of code that did almost the same: iterating over collections of our business objects in order to perform basically the same set of more or less complex tasks.

We also found some of those loops was particularly hard to read. Developers spent more time trying to figure out what a given loop did than to write the loop itself.

For this reason I started writing some utility methods and implementing a small DSL at the purpose to improve the code readability. The guys of my team liked this approach and felt comfortable with it almost immediately.

But when they start asking “How could I wrote this in your collections language?” or “Could you add that feature to your collections thing?” I realized I was writing something that could have a very large and general applicability field. So I decided to refactor it in an independent library outside the original project.

Of course the last step was to find a name for that library, since in my opinion “the Mario's collection thing” was not good enough. I choose lambdaj since to develop it I used some functional programming techniques that in turns derive from the lambda-calculus theory.