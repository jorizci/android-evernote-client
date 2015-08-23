# android-evernote-client
Android application capable of accessing Evernote accounts to read existing notes and publish new ones.

# Purpouse
This is a self educational application to test the new Evernote Android SDK. The planned objectives for
this application are:

  - Login as a standard user
  - List of all notes
  - View note content
  - Create new notes

# Current state
Currently the aplication is completelly functional. There is a versios without ocr support (master branch) and one with OCR support with tesseract libraries included in the project (ocr-feature)

##Currently implemented funcionality
  - Login as a standard user
  - List user created notes
  - Order user created notes
  - View user created notes
  - Create new notes
  - Create new notes with supplied written text

# Configuration
Android project is configured to work in Android Studio through gradle builder.
To compile correctly the user needs to define these variables on user gradle.properties
or project.

  - EVERNOTE_CONSUMER_KEY=[VALID KEY]
  - EVERNOTE_CONSUMER_SECRET=[VALID SECRET]
  - EVERNOTE_SERVICE=com.evernote.client.android.EvernoteSession.EvernoteService.SANDBOX

The valid key and valid secret are the api keys that have to be obtained from evernote developer webpage. The api key has to be created with the name of the application 'EvernoteClientApp'
