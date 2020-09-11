## Covid 19 Tracker App

##### This is a admin pannel for [Covid-19 Tracker App](https://electron.atom.io) along with api from [covid-19-api-myanmar](https://electron.atom.io)

## Tech

##### Covid 19 admin pannel uses a number of lib and open source project to work properly :3

- [JFoenix](http://www.jfoenix.com/) - Javafx material design library,that make our ui freaking cool
- [FontAwesome fx](https://github.com/Jerady/fontawesomefx-glyphsbrowser) - FontAwesome icons for javaFx
- [Animtate-Fx](https://github.com/Typhon0/AnimateFX) - css anitmate.css lib for javafx
- [OkHttp](https://square.github.io/okhttp/) - HTTP is the way modern applications network. It’s how we exchange data & media. Doing HTTP efficiently makes your stuff load faster and saves bandwidth
- [JSON-java](https://github.com/stleary/JSON-java) - JSON in Java make JavaObject to json and json to java Object ez

#### Use Of Tech

- This Project is written in Java and using javaFX for nice GUI.

#### Sofware Pattern

- for simplicity and clean code we follow [MVC](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller) design pattern,as I told above I use [covid-19-api-myanmar](https://electron.atom.io)(for details check this) for storing and manipulating data.
  You can found CRUD Request on Model Folder/Package,
  It hard to sepearate views and controller in GUI base app,So user action and logic are written in controller
  On Views folder,some fxml file for UI

#### Authentication & Authorization

- First you need to Authenticate as admin ,
- Admin can create another admin(I know that sound funny)
- Once you authenticate you don't need to login again we use authentication streategy call [JWT](https://jwt.io/) to authorize admin

## Screenshots

### Login View

![Login View](https://github.com/mohamadealiyes/covid19adminjava/blob/master/src/views/Images/loginView.png)

### Forget Password View

![Login View](https://github.com/mohamadealiyes/covid19adminjava/blob/master/src/views/Images/forgetView.png)

### Admin View

![Login View](https://github.com/mohamadealiyes/covid19adminjava/blob/master/src/views/Images/AdminView.png)

## Credits

Thanks some of my members for paying attention and filling data,making uml,use case,class diagram,presentation slides and others stuff

- [Myint Zuu Kyaw]()
- [Lin Htet Aung]()
- [Khin Nyein Wai]()
- [Thu Thu San]()
