# todo list in kotlin
todo list demo project created in kotlin using room database. and also added notification to notify user at specific date




##ASSUMPTIONS

1. The server has already been built and takes and sends data in a json format which is being used by the app
2. The server has end points ready and they would be provided to me in future so that i can add them and those endpoints will be fired from the app

##DESCRIPTION
###<B><I><U>Android Studio 4.1.3 | Kotlin 1.3.50 | AGP 3.4.2  | Gradle 5.1.1 | JDK 11 | Compile SDK 29 </BIU>
1. All the setting and configuration has been been done for firing the apis just need the BASE URL and endpoints to work.
2. App has been configured with the security features for firing of apis which include Auth Token and Refresh Auth Token and their expiry time
3. App has been added with 2 Api at the moment which include tracking and sending analytics for 3 features :
  i. Adding a new todo task
       App will send all the new Data and additional variables to the backend like is the task being edited or added 
 ii. Editing an old task
       App will send all the previous Data before the edit and the new Data and additional variables to the backend like is the task being edited or added  
iii. Deleting a todo task
       App will send all the previous Data which was deleted right now

4. Logging, Error Handling and Auth Token Management for network calls has been added.
5. All of the Code has been seeminglessly been slided into the previous codebase without disturbing it.
6. All of the App has been BUILT to Follow <B>MVVM-ModelViewViewmodel</B> Structure
7. Dependency Injection has been done in the app without using any thirs library.




##DEPENDENCY

1. Retrofit -- Used for network calls
2. Coroutines -- Used for async and concurrent programming
3. retrofit okhttp logging -- for logging of network calls
4. retrofit Gson-- Used for converting the Json directly to model classes.


##CHANGES  
1. Created some packages and refactored the destinations for some files according to those packages to make the project easier to understand for example UI,VIEWMODEL etc.
2. Did changes in viewmodel, Application class, Main Activity,manifest except for them just refactoring changes for added packages are there.
3. Added new files for better SOLID  principle approch and maintaining structure of the project like
    Api Interface - for firing API 
    AuthInterceptor - for Authentication 
    MyApplication - for Global Variable without wasting memory
    Repository    - for Firing APi according to MVVM 
    SessionManager - For Managing Session 
    TodoData      -  for sending the data to the server 
    TodoViewModelFactory - for Injecting the ViewModel with Dependency 
    TokenResponse - for Getting the token response from the server 
     
