<script src="https://www.google.com/jsapi?key=ABQIAAAA26vPnnJWBcCG6cgCQi8iFxTJQa0g3IQ9GZqIMmInSLzwtGDKaBSeNhheb8B4HlYnHp4u2bnwU1jScQ" type="text/javascript"></script>
<script language="Javascript" type="text/javascript">
//<![CDATA[

google.load("search", "1");

function OnLoad() {
  // Create a search control
  var searchControl = new google.search.SearchControl();

  // Add in a full set of searchers
  var localSearch = new google.search.LocalSearch();
  searchControl.addSearcher(localSearch);
  searchControl.addSearcher(new google.search.WebSearch());
  searchControl.addSearcher(new google.search.VideoSearch());
  searchControl.addSearcher(new google.search.BlogSearch());

  // Set the Local Search center point
  localSearch.setCenterPoint("New York, NY");

  // Tell the searcher to draw itself and tell it where to attach
  searchControl.draw(document.getElementById("searchcontrol"));

  // Execute an inital search
  searchControl.execute("Google");
}
google.setOnLoadCallback(OnLoad);

//]]>
</script>