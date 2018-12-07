# Movie Recommender 

This project  creates a simple recommend system utilizing Java. 
It works with movies and movie ratings, but the principles involved can be adapted to books,restaurants, and more. 
The project determines which movies should be recommended to a user based on the ratings of several movies by people of similar age.

## Getting Started

Git clone the project and run the main() function in MovieRecommend.java


### Input files

#### NewUsers.csv

The first five lines are shown below as an illustration. 5 indicates the best rating and 1
indicates the worst rating.

```
 UserID,UserName,UserAge,MovieID,MovieName,Rating
 1,Adam,15,,"1,dinosaur planet",3
 2,Amir,20,,"2,isle of man tt 2004 review",2
 3,Brad,25,,"3,character",1
 4,Krishna,35,,"4,paula abdul's get up & dance",4
 5,Sangy,21,,"5,the rise and fall of ecw",5
```
#### RatingInput.csv

This is the second input CSV file which lists some new users, their age and the
number of movies they want recommended to them. The file also has a question mark (?) which you will
replace with movie recommendations.

```
UserName,UserAge,NoOfMoviesToRecommend,Movies
    Xin,20,4,?
    Erin,24,3,?
    Sri,37,2,?
    Nick,18,4,?
    Vicky,20,2,?
```

