Deployment link: https://colonial-meryl-a-samuel-indriano-2406400524-72bb247d.koyeb.app/
(Forgot to add the link here for module 02)

Reflection 4 (Module 4)
1. After finishing this tutorial, I felt that TDD workflow was useful for me because it forced me to analyze the expected behaviour before writing any new implementation / feature. By writing tests first for Order model, repo, and service, I could focus on one requirement at a time and get immediate feedback after each small change. This made it easier to find any mistakes earlier. But I also felt that sometimes I didn't understand the requirements fully before writing the test. This made me unsure about the expected value, so I spent more time just to adjust the tests and implementation. So next time I should spend more time identifying the inputs, outputs, happy paths, and unhappy paths fully from the start.

2. Yeah, tests for Order have successfully followed the F.I.R.S.T principle. They implemented the Fast principle by using simple, short, and effective tests. They implemented the Independent principle by separating each behaviour into different test functions. They implemented the Repeatable principle because the tests use fixed inputs and can produce the same result every time they run. They implemented the Self-Validating principle because each test uses assertions to show whether the result is correct or not. Lastly, the tests also implemented the Timely principle because the tests were made before the implementation process (TDD workflow).

Reflection 3 (Module 3)
1. - I applied SRP by seperating CarController from ProductController and moving car ID generation logic from
CarRepository to CarServiceImpl. This changes will make every class has single responsibility.
- I applied LSP by removing the inheritance between CarController and ProductController because CarController shouldn't be treated as a subtype of a ProductController.
- I applied DIP by making CarController depend on CarService interface instead of CarServiceImpl. I also made the service layer depend on repository abstraction instead of concrete repository classes.
- I applied ISP by splitting repository abstractions into smaller interfaces, such as CarReadRepository, CarWriteRepository, ProductReadRepository, and ProductWriteRepository. This make sure that every functions that implemented those interface will only depends on the operations that it actually needs.
- I applied OCP by creating generic repository contracts, such as ReadRepository<T, ID> and WriteRepository<T, ID>. Now, entity repository interfaces such as Car and Product can extend them and new entities can be added more easily without having to change the existing structure.

2. Applying SOLID principles makes the code easier to maintain, extend, and understand. For example, after separating CarController from ProductController, each controller now only handles its own domain. Now, every class has a clearer responsibility. Then, by moving the Car ID generation logic from CarRepository to CarServiceImpl makes the design clearer, so now the repository no longer contains business logic. Lastly, using interfaces like CarService, CarReadRepository, CarWriteRepository, ProductReadRepository, ProductWriteRepository keeps the design flexible. Flexible design means that if any changes need to be applied later, I can add new implementations without need to rewrite all other classes that depend on them.


3. If I don't apply SOLID principles, my code will be harder to maintain and harder to extend in the future.
For example, if CarController and ProductController are still together in one class, then the responsibility of the class becomes unclear. Unclear responsibility of the class could cause side effects, where changing a feature in one place could break features in other places. If CarController still depends on CarServiceImpl, then the controller becomes too dependent on one concrete class and the code becomes less flexible. If I don't split repository interfaces into smaller interfaces, then some classes may depend on methods that they do not actually need. Because of that, the code can become more confusing, more tightly coupled, and more difficult to test or modify when I want to add new features later.


Reflection 2 (Module 2)
1. In this exercise, I selected 3 code quality issues from SonarCloud findings. The first issue was a critical severity issue, which there were some duplicated literal "redirect:/product/list" in my code. I resolved it by extracting the value into a constant and reusing it in many of the related methods. The second issue was a critical severity also, which there was an empty setup method. I resolved it by removing the empty setup method. The third one was a minor severity, which I resolved by removing an unused MockBean import. My strategy was to prioritize high-severity issues first, then apply some refactoring, and verify the result by rerunning the test.

2. I think my current implementation has met the definition of Continuous Integration. Every push and pull request triggers an automated test execution, and SonarCloud analysis is also integrated, so code quality issues can be detected before changes are finalized. This gives fast feedback and helps prevent bad code from being merged. For deployment, I also implemented CD using a pull-based approach with Koyeb. Koyeb is connected to my repository main branch, then redeploys automatically when changes are merged to main branch.


Reflection 1.1 (Module 1)
1. Clean code  
I have learned to apply and understand clean code principles while writing my code. One of the main things
I focused on was using clear and descriptive variable names, so the variable names are self-explanatory
without needing any comments to explain. I also applied the principals of using functions, such as clear and 
descriptive names, and focus on one specific task.

2. Security  
I applied REST principles to ensure the security of my application. For example, I used GET only for 
retrieving data. Then I used POST for modifying data, such as create, edit, or deleting a product.

3. Mistakes  
A mistake I made several times was rushing to commit to Git without realizing that some of the logic
in my code were still incomplete. To prevent this from happening again, I'll try to be
more careful by checking the completeness of the logic and bug testing before finally committing to Git.

Reflection 1.2 (Module 1)
1. Implementing unit test felt quite difficult at first, but eventually I started to understand it.
I also feel more confident because unit test help to verify the correctness of my code logics. I feel
like the number of unit tests depends on how much the code coverage. For example, 90% code coverage is already
very good. However, 100% code coverage doesn't guarantee that there are no bugs or errors in my code.

2. If I create a new functional test suite with the same setup procedures and instance variables from
CreateProductFunctionalTest, the code become less clean because it can cause duplicated code.
Duplicated code can reduce code quality because if I need to change the logic, I have to update it in 
many places and could easily miss one place. To improve it, I should reuse the setup in a cleaner way,
such as putting the setup into a helper method, so each test suite that uses same setup is easier
to maintain.
