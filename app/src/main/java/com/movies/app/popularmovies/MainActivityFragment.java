package com.movies.app.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private MovieAdapter movieAdapter;
    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    //    super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_popular)
        {
            return true;
        }
        if(id==R.id.action_top_rated)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String []moviesArray= {"MONDAY","TUESDAY","WENESDAY","THURSDAY"};

        List<String>listmovie=new ArrayList<String>(Arrays.asList(moviesArray));
        new FetchMovieTask().execute();
        movieAdapter=new MovieAdapter(getActivity(),R.layout.grid_item_movies,R.id.movie_poster,listmovie);
        View rootview=inflater.inflate(R.layout.fragment_main,container,false);

        GridView gridView= (GridView) rootview.findViewById(R.id.gridview_movies);

        gridView.setAdapter(movieAdapter);

        return rootview;
        //return inflater.inflate(R.layout.fragment_main, container, false);
    }

    class FetchMovieTask extends AsyncTask<Void,Void,String[]>
    {
        MovieObject [] movieObjects=null;
        String []str=null;

        @Override
        protected void onPostExecute(String[] str) {
            if(str!=null)
            {
                movieAdapter.clear();
                for(String s : str)
                {
                    movieAdapter.add(s);
                }
            }
        }

        @Override
        protected String[] doInBackground(Void... strings) {



            String api_key="APIKEY";
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJSONStr = null;
            try {
                URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="+api_key);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    movieJSONStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    movieJSONStr = null;
                }
                movieJSONStr = buffer.toString();
                // Log.v("MY JSON OUTPUT", forecastJSONStr);
                String title="title";
                String vote_average="vote_avergae";
                String overview="overview";
                JSONObject movieJSONObject=new JSONObject(movieJSONStr);
                JSONArray movieJSONArray=movieJSONObject.optJSONArray("results");
//                movieObjects=new MovieObject[movieJSONArray.length()];
                str=new String[movieJSONArray.length()];
                for(int i=0;i<movieJSONArray.length();i++)
                {

                    JSONObject jsonObject=movieJSONArray.getJSONObject(i);
                   // movieObjects[i].title=jsonObject.optString("title").toString();
                   // movieObjects[i].overview=jsonObject.optString("overview").toString();
                   // movieObjects[i].poster_path=jsonObject.optString("poster_path").toString();
                   // movieObjects[i].release_date=jsonObject.getString("release_date").toString();
                   // movieObjects[i].vote_average=jsonObject.getString("vote_average").toString();
                    str[i]=jsonObject.getString("poster_path");
                }
                for(String s : str)
                {
                    Log.v("POSTER PATH",s);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return str;


          //  return null;
        }
    }
}
