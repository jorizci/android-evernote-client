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
Currently the application is being developed with developer token. This means that at this moment the application can only communicate and apply changes only to the specific account that is linked to that token.

##Currently implemented funcionality
  - Create new notes

# Configuration
Android project is configured to work in Android Studio through gradle builder.
To compile correctly the user needs to define these variables on user gradle.properties
or project.

  - EVERNOTE_DEVELOPER_TOKEN= [appropiate developer token]
  - EVERNOTE_NOTE_STORE= [appropiate note store url]
  - EVERNOTE_SERVICE=com.evernote.client.android.EvernoteSession.EvernoteService.PRODUCTION
