package Controller;


import model.Song;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.Optional;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.io.*;
import java.util.Collections;
public class SongLibController {

    @FXML TextField addAlbum;
    @FXML TextField addArtist;
    @FXML Button add;
    @FXML TextField addName;
    @FXML Text addTit;
    @FXML TextField addYear;
    @FXML Button delete;
    @FXML TextField editAlbum;
    @FXML TextField editArtist;
    @FXML Button edit;
    @FXML TextField editName;
    @FXML Text editTit;
    @FXML TextField editYear;
    @FXML ListView<Song> songList;
    @FXML Text title;
    
    private ObservableList<Song> obsList ;
	int n = 0;
  
    public void start(Stage mainStage) { 
    	obsList = FXCollections.observableArrayList(); 

		//read songs from file
		//if(file.length() != 0){
			try {
				readSongs();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			songList.setItems(obsList); 
    		songList.getSelectionModel().select(0);
    		itemsDisplay();
			songList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> itemsDisplay());// test
			 
		}
   // }
        
    public boolean songCheck (ObservableList<Song> obsCheck, Song Check) {
    	for(int i = 0; i < obsCheck.size(); i++) {
    		if (obsCheck.get(i).getName().toLowerCase().equals(Check.getName().toLowerCase()) 
    			&& obsCheck.get(i).getArtist().toLowerCase().equals(Check.getArtist().toLowerCase())){
    			return true;
    		}
    	}
    	return false;	
    }
    
    public boolean songCheckEdit (ObservableList<Song> obsCheck, Song Check, int index) {
    	for(int i = 0; i < obsCheck.size(); i++) {
    		if ( i != index) {
    			if (obsCheck.get(i).getName().toLowerCase().equals(Check.getName().toLowerCase()) 
    					&& obsCheck.get(i).getArtist().toLowerCase().equals(Check.getArtist().toLowerCase())){
    				return true;
    			}
    		}
    	}
    	return false;	
    }
    public void readSongs() throws IOException{
	   	File file = new File("src/songs.txt"); 
	   	Scanner readFile = new Scanner(file);

		StringTokenizer s = null;
		
		String name = "", artist = "", album= "", year = "";

		while (readFile.hasNextLine()) {
			s = new StringTokenizer(readFile.nextLine(),"|");
			
			name = s.nextToken();
			artist = s.nextToken();
			album = s.nextToken();
			year = s.nextToken();
			
			Song newSong = new Song(name,artist,album,year);
			obsList.add(newSong);
			n++;
		}
	   	
	   	readFile.close();
	   	songList.setItems(obsList);
	}

	//writes each song to file
	public void writeFile(){
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("src/songs.txt", "UTF-8");
			writer.close();
			writer = new PrintWriter("src/songs.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < obsList.size()-1 ; i++){
			if(!obsList.get(i).getName().equals("")){
				writer.print(obsList.get(i).getName() + "|" + 
				obsList.get(i).getArtist() + "|" + 
				obsList.get(i).getAlbum() + "|" + 
				obsList.get(i).getYear() + "|"  + "\n");
			}
		}
		
		if(!obsList.get(obsList.size()-1).getName().equals("")){
			writer.print(obsList.get(obsList.size()-1).getName() + "|" + 
			obsList.get(obsList.size()-1).getArtist() + "|" + 
			obsList.get(obsList.size()-1).getAlbum() + "|" + 
			obsList.get(obsList.size()-1).getYear() + "|");
		}
		
		writer.close();
	}
	
    public void buttonActions(ActionEvent e) {
        //check which button was pressed
		Button b = (Button)e.getSource();
		Alert error = new Alert(AlertType.ERROR);
		Alert confirm = new Alert(AlertType.CONFIRMATION);
		int i = songList.getSelectionModel().getSelectedIndex();
		int j = i;
		Song newSong = new Song();
		Song oldSong = new Song();
		
		//actions
		if (b == add) {
			//ADD TO LIST

			String nameAdd,artistAdd,albumAdd,yearAdd;
			nameAdd = addName.getText().trim();
			artistAdd = addArtist.getText().trim();
			albumAdd = addAlbum.getText().trim();
			yearAdd = addYear.getText();
			newSong = new Song(nameAdd, artistAdd, albumAdd, yearAdd);
			
			//check if user input is valid
			if(checkInput(nameAdd, artistAdd, albumAdd, yearAdd) == false) return;

			if (songCheck(obsList,newSong) == true){
				error.setTitle("ERROR");
				error.setHeaderText("Enter different song");
				String content = "This song is already in the library";
				error.setContentText(content); 
				error.showAndWait();
				return;
			} else if(nameAdd.isEmpty() || artistAdd.isEmpty()){
				error.setTitle("ERROR");
				error.setHeaderText("Enter name of song and artist name");
				String content = "One or more fields is empty";
				error.setContentText(content); 
				error.showAndWait();
				return;
			}else {
				confirm.setTitle("Add");
				confirm.setHeaderText("Are you sure you want to add " + nameAdd + "to your song library?");
				String content = ("Name: " + nameAdd + "\nArtist: " + artistAdd + "\nAlbum: " + albumAdd + "\nYear: " + yearAdd);
				confirm.setContentText(content); 
				
				//check what they said
				Optional<ButtonType> result = confirm.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.CANCEL) {
					itemsDisplay();  
					return;
				}

				//check if they added album name or year
				if(albumAdd.isEmpty()) albumAdd = "N/A";
				if(yearAdd.isEmpty()) yearAdd = "N/A";
				newSong = new Song(nameAdd, artistAdd, albumAdd, yearAdd);

				obsList.add(newSong);
				n++;
			}

		}else if(b == edit){
			//EDIT LIST


			String newName, newArtist, newAlbum, newYear;
			newName = editName.getText();
			newArtist = editArtist.getText();
			newAlbum = editAlbum.getText();
			newYear = editYear.getText();
			newSong = new Song(newName, newArtist, newAlbum, newYear);
			
			//check if list is empty
			if(obsList.isEmpty()){
				error.setTitle("ERROR");
				error.setHeaderText("The song list is empty");
				String content = "the library is empty";
				error.setContentText(content); 
				error.showAndWait();
				return;
			}

			//check if there are empty fields
			else if(newName.isEmpty() || newArtist.isEmpty()){
				error.setTitle("ERROR");
				error.setHeaderText("Enter song name and artist");
				String content = "One or more fields is empty";
				error.setContentText(content); 
				error.showAndWait();
				return;
			} 
			
			//check if exists
			else if(songCheckEdit(obsList,newSong,i) == true) {
				error.setTitle("ERROR");
				error.setHeaderText("Enter different song");
				String content = "This song is already in the library";
				error.setContentText(content); 
				error.showAndWait();
				
				return;
			}
			
			else{
				//confirm
				confirm.setTitle("Edit");
				confirm.setHeaderText("Are you sure you want to change this song to " + newName + " ?");
				String content = ("New Name: " + newName + "\nNew Artist: " + newArtist + "\nNew Album: " + newAlbum + "\nNew Year: " + newYear);
				confirm.setContentText(content); 

				//check what they said
				Optional<ButtonType> result = confirm.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.CANCEL) {
					itemsDisplay();  
					return;
				}
				
				//change song
				oldSong = obsList.get(i);
				obsList.set(i, newSong);
			}
		}else if(b == delete){
			//DELETE FROM LIST

			int h = songList.getSelectionModel().getSelectedIndex();
			
			//check if list is empty
			if(obsList.isEmpty()){
				error.setTitle("ERROR");
				error.setHeaderText("Nothing in library");
				String content = "You are trying to delete nothing";
				error.setContentText(content); 
				error.showAndWait(); 
				return;
			}else{
				//confirm
				confirm.setTitle("Delete");
				confirm.setHeaderText("Are you sure you want to delete " + obsList.get(h).getName() + " from your song library?" );
				String content = "Name: " + obsList.get(h).getName() 
											+ "\nArtist: " + obsList.get(h).getArtist() 
											+ "\nAlbum: " + obsList.get(h).getAlbum() 
											+ "\nYear: " + obsList.get(h).getYear();
				confirm.setContentText(content); 

				//check what they said
				Optional<ButtonType> result = confirm.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.CANCEL) {
					itemsDisplay();  
					return;
				}

				//delete song
				ObservableList<Song> newList = FXCollections.observableArrayList(); 
				newList.addAll(obsList);
				newList.remove(i);
				songList.setItems(newList);
				obsList = newList;
			}
		}

		//highlighting song
		
		if(obsList.isEmpty()){} 
		else if(obsList.size() == 1){
			songList.setItems(obsList);
			songList.getSelectionModel().select(0);
			songList.scrollTo(0);
		}else{
			Collections.sort(obsList, Song.sortArtist);
			Collections.sort(obsList, Song.sortName);
		
			if(b == add){
				j = obsList.indexOf(newSong);
			}else if(b == edit){
				j = i;
				//j = obsList.indexOf(oldSong);
				//System.out.println(j);
			}else if(b == delete && i<obsList.size()){
				j = i;
			}else{
				j = i-1;
			}
			
			songList.getSelectionModel().select(j);
			songList.scrollTo(j);
		}

		//add to file
		songList.setItems(obsList);
		writeFile();
		return;
    }
  
    public void itemsDisplay() {
    	//int index = songList.getSelectionModel().getSelectedIndex();
    	
    	if(obsList.isEmpty()) {
    		editName.setText("");
    		editArtist.setText("");
    		editAlbum.setText("");
    		editYear.setText("");
    	} else if(obsList.size() == 1) {
    		editName.setText((obsList.get(0).getName()));
    		editArtist.setText((obsList.get(0).getArtist()));
    		editAlbum.setText((obsList.get(0).getAlbum()));
    		editYear.setText((obsList.get(0).getYear()));
    	} else {
    		int index = songList.getSelectionModel().getSelectedIndex();
    	editName.setText((obsList.get(index).getName()));
		editArtist.setText((obsList.get(index).getArtist()));
		editAlbum.setText((obsList.get(index).getAlbum()));
		editYear.setText((obsList.get(index).getYear()));
    	}
		
		addName.setText("");
		addArtist.setText("");
		addAlbum.setText("");
		addYear.setText("");
    }

	public boolean checkInput(String name, String artist, String album, String year){
		Alert error = new Alert(AlertType.ERROR);

		//check if they entered song name and artist
		//also check it doesnt contain '|'
		if(name.isEmpty() || artist.isEmpty()){
			error.setTitle("ERROR");
			error.setHeaderText("Enter song name and artist");
			String content = "One or more fields is empty";
			error.setContentText(content); 
			error.showAndWait();
			return false;
		}else if(name.contains("|") || artist.contains("|")){
			error.setTitle("ERROR");
			error.setHeaderText("Check for '|'");
			String content = "Name or artist contains '|'";
			error.setContentText(content); 
			error.showAndWait();
			return false;
		}
		
		if((!album.isEmpty() || album == "N/A") && album.contains("|")){
			error.setTitle("ERROR");
			error.setHeaderText("Check for '|'");
			String content = "Album contains '|'";
			error.setContentText(content); 
			error.showAndWait();
			return false;
		}

		//check if year is a positive integer
		if(!year.isEmpty() || year == "N/A"){
			try{
				int i=Integer.parseInt(year);  
				if(i<0) return false;
			}catch(Exception e){
				error.setTitle("ERROR");
				error.setHeaderText("Enter year as positive integer");
				String content = "Year is not positive integer";
				error.setContentText(content); 
				error.showAndWait();
				return false;
			}
		}
		
		return true;
	}
}
