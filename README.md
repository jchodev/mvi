# mvi

Using Library:\
Dependency injection : hilt With dagger\
Networking : retrofit2\
Test with flow : turbine\
Image load:  glide\
Navigation lib: androidx.navigation:navigation
Testing: mockito, com.squareup.okhttp3:mockwebserver


# Simple flow
1. User (View)  →  send Input  →   ViewModel class (or said presenter class?)
2. ViewModel class Received Intent, base on type to doing different process: \
   a. Let UI showing loading (via viewState.loading)\
   b. try to go data from Repository\
   c. Let UI show data / show error message (base on result)
