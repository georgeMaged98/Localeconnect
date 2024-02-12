# LocaleConnect Project Documentation

![LocaleConnect Home Page](screenshot/home page.PNG "LocaleConnect Home Page")

## Description

LocaleConnect envisions being the ultimate gathering spot for travelers, akin to how LinkedIn connects professionals. It serves as a vibrant community where travel enthusiasts and local experts exchange experiences, recommend the best spots to visit in cities or countries, encourage one another, and meet fellow travelers during their explorations.

## Features

### User Service
- Registration options for Travelers and Local Guides.
- Login/logout capabilities.
- Functionality to follow/unfollow other users.
- View list of followings.
- Update or delete account features.
- Travelers can rate Local Guides.
- Display Local Guides' ratings and counts.

### Trip
- Exclusive trip creation by Local Guides.
- Comprehensive list and details of trips accessible to users.
- Attend/unattend trips functionality.
- Real-time notifications for updates or deletions of joined trips.
- Trip sharing option.

### Itinerary
- Itinerary creation by Users.
- Comprehensive list and details of itineraries accessible to users.
- Attend/unattend itineraries functionality.
- Real-time notifications for updates or deletions of joined itineraries.
- Itinerary sharing option.

### Meetup
- Meetup creation by Users.
- Comprehensive list and details of meetups accessible to users.
- Attend/unattend meetups functionality.
- Real-time notifications for updates or deletions of joined meetups.
- Meetup sharing option.

### Feed
- Post creation with text content and image support (GCP storage).
- Personalized feed showcasing posts from the user and their followings.
- Timeline feature for following users' posts.
- Like/dislike, comment, and view like count functionalities.

### Notification Service
- Real-time notifications with Web-Sockets for new followers or updates/deletions to registered trips or meetups.

### GCP Storage Service
- Image storage for trips, user profiles, meetups, etc.

### Repository Cloning
1. **Clone the Repository**: Run `git clone https://gitlab.lrz.de/MoetazKhelil/localeconnect.git`

## Backend Setup Guide for LocaleConnect

### Prerequisites
- **Java JDK 17**: Required for running Spring Boot. Verify or install from [Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or [AdoptOpenJDK](https://adoptopenjdk.net/?variant=openjdk17).
- **Maven**: Used for dependency management. Check with `mvn -v` or install from [Maven](https://maven.apache.org/install.html).
- **IDE**: IntelliJ IDEA, Eclipse, or Spring Tool Suite recommended for development.

### Running the Backend with Docker
- Install Docker Desktop.
- **Docker Login**: Run `docker login` and provide the credentials:
    - Username: `localeconnect`
    - Password: `lssa2324app`
- Navigate to `/LocaleConnectBackend` and execute `docker-compose -f docker-compose-test.yml up` to start containers.

## Frontend Setup Guide for LocaleConnect

### Installation
Make sure to install the following:
- Node (version 10.2.4): to check run => `npm -version`
- Angular (version 15.0.0)
- Angular Material

### Before Running the App
Run `npm install`

### LocaleConnectFrontend
This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 15.0.0.

#### Development server
Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

#### Code scaffolding
Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

#### Build
Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

#### Running unit tests
Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

#### Running end-to-end tests
Run `ng e2e` to execute the end-to-end tests via a platform of your choice. To use this command, you need to first add a package that implements end-to-end testing capabilities.

#### Further help
To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI Overview and Command Reference](https://angular.io/cli) page.

## Security
Utilizes JWT for authentication. Users access other services post-registration/login through the API gateway. Implemented in the User Service.

## Postman Documentation
[View Postman Documentation](https://www.postman.com/cloudy-shadow-750794/workspace/new-team-workspace/overview)

## Testing
Run `mvn clean test` to execute the tests.

## Contributors
This project is brought to life by the dedication and expertise of George Elfayoumi, Maha Marhag, and Moetaz Khelil.
