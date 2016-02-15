
Live URL on Heroku https://blooming-meadow-73722.herokuapp.com

Application Architecture

-Application builded using Java,Spring Boot,Jquery with Tomcat as server to deploy and hosted on Heroku.

Assumption:

To find issues related to pertcular repository its assumed b user will provide proper URL in below format
-http://github.com/{org or owner}/{repo}
-https://github.com/{org or owner}/{repo}

How it Works?

Based upon url application will generate first URL according to github API standards and make a rest call to GitHub to get repository info.Repository Info URL looks like this 
Ex- https://api.github.com/repos/Shippable/support

After receiving successfull response totalOpenIssues count is used for pagination as GitApi supports maximum 100 per page.
For next subsequent requests URL is formed based upon  totalOpenIssues.
For Example:

if totalOpenIssues=272 then  we will make 3 subsequent request to get all issues.
  Reuests will look like this
  
  https://api.github.com/repos/Shippable/support/issues?page=1&per_page=100
  https://api.github.com/repos/Shippable/support/issues?page=2&per_page=100
  https://api.github.com/repos/Shippable/support/issues?page=3&per_page=100
  
  Based on issue created date the desired result is calulated.
  
What are possible improvements?

1.If we think in real world scenario the issue raised on any re pository say at peak day 100 and then total issues per week   say 600.Using super human power if we fix max issues still its possible we will remain with 300.
  In this case with above approach we will  end up reading all issues then filtering it.Something like this
  https://api.github.com/repos/Shippable/support/issues?page=1&per_page=100&since=XYZDate
  we will end up reading very little data with better through put.
  
  Example:  https://github.com/Shippable/support/issues
   For this there are 272 issues open.With this approach just we need to read issues raised in past 24 hour and this week
   which are 7 in this case.
   
2.We can add consumer producer mechanism to handlne large data.Currently its waits to finish its processing then make an     next call.

3.We can come up with generic model which can perform all kind of operation on given repository.




