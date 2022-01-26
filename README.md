# issue_6749_android

- Connect to a physical device using a cable.
- Before debugging run `adb tcpip 5555 && adb connect <device ip>`
- **Remove the cable.**
- Now run it in debug mode.
- Verify it can write a document.
- Now put it into standby mode.
- Wait 20-30'ish minutes (in which time Android will shut down Wifi to save power).
- Wake it up and add a document again.

You can see the details in Logcat, at the error-level.

**The interesting details to look for, is the difference in time between the local timestamp and the server timestamp.**
