# first-mobile-app
A school assignment project for a Mobile App Development course

## Assignment 1 - Requirements
  - Activity 1 (Show All Advices(Objects))
    - It should show all the currently available Advices(Objects) and allow the user to switch to the next activity.
    - For each Advice(Object) the content and author should be shown (- my edit, I added the category on the side)
    - The activity should allow the user to select a specific category and filter the advice list accordingly.
    
  - Activity 2 (create a new Advice(Object))
    - Selecting Ok should return to the "Show" activity, with the new Advice added.
    - Selecting Cancel should also return  to the "Show" activity, but obviously without showing the new advice.
    - You may either provide a mechanism to select the category for the new advice, or assume that the currently selected category in the "Show activity should be used.
    
    
## Assignment 2 - Requirements
   - In **portrait** orientation the application should behave as Assignment 1, but now only
      using *one activity* which switches between **two fragments** using a navigation graph.
   - In **landscape** orientation the application should also use one activity, but show the two
      fragments side-by-side: the list of advices on the left half of the screen, and the editor for
      entering a new advice on the right half of the screen.
   - Use a **ViewModel** to store the list of advices.

# Current status
  - Assignment 1 completed :heavy_check_mark:.
  - Assignment 2 completed :heavy_check_mark:.