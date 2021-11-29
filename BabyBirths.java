/*
Subject : giving a file ranking baby names in the US in one year, the input would
         be a name (lets say ranks 16 in 2014), we should find the corresponding name
         that rankes 16 in another year (say 1945).

@author : WALID BAKIR
Part of the Coursera java course Specialization. Oriject Baby Names.

*/

import java.io.*;
import edu.duke.*;
import org.apache.commons.csv.*;

public class BabyBirths{


// #############################################################################
  public void printNames(){
    FileResource fr = new FileResource();
    CSVParser parser = fr.getCSVParser(false);
    // false means the csv file doesnt have a header
    // fields would be accesed using an index.
    for (CSVRecord record : parser){
      int borns = Integer.parseInt(record.get(2));
      if (borns <= 1000 && borns >=700){
        System.out.println("Name : " + record.get(0) + " Gender : "
                           + record.get(1) + " NumberBorns : " + borns);
      }
    }
  }

  public void printNames2(){
    FileResource fr = new FileResource("us_babynames_by_year/yob2012.csv");
    CSVParser parser = fr.getCSVParser(false);
    // false means the csv file doesnt have a header
    // fields would be accesed using an index.
    for (CSVRecord record : parser){
      int borns = Integer.parseInt(record.get(2));
      if (record.get(1).equals("F") && borns > 10000){
        System.out.println("Name : " + record.get(0) + " Gender : "
                           + record.get(1) + " NumberBorns : " + borns);
      }
    }
  }


  public void totalbirths(FileResource fr){
    int totalBoys = 0;
    int totalGirls = 0;
    CSVParser parser = fr.getCSVParser(false);
    for (CSVRecord record: parser){
      // int borns = Integer.parseInt(record.get(2));
      if (record.get(1).equals("F")){
        totalGirls += 1;
      }
      else {
        totalBoys += 1;
      }
    }
    int totalBirths = totalBoys + totalGirls;
    System.out.println("Total births : " + totalBirths);
    System.out.println("Total Boys : " + totalBoys);
    System.out.println("Total Girls : " + totalGirls);

  }

// #############################################################################
  public int getRank(int year, String name, String gender){
    // returns the rank of the name (for a male and a female) in the giving year.
    int rank = 0;
    FileResource fr = new FileResource("us_babynames_by_year/yob" + year + ".csv");
    CSVParser parser = fr.getCSVParser(false);
    for (CSVRecord record : parser){
      if (record.get(1).equals(gender)){
        rank += 1;
        if (record.get(0).equals(name)){
          return rank;
        }
      }
    }
    return -1;
  }


public void testGetRank(){
  String name = "Emily";
  String gender = "F";
  int year = 1960;
  System.out.println("The rank of the name : " + name + " in the year " + year +" is " + getRank(year,name,gender));
  name = "Frank";
  gender = "M";
  year = 1971;
  System.out.println("The rank of the name : " + name + " in the year " + year +" is " + getRank(year,name,gender));
}


// #############################################################################

public String getName(int year, int rank, String gender){
  // get the name ranking (rank) in the year (year).
  int count = 0;
  FileResource fr = new FileResource("us_babynames_by_year/yob" + year + ".csv");
  CSVParser parser = fr.getCSVParser(false);
  for (CSVRecord record : parser){
    if (record.get(1).equals(gender)){
      count += 1;
      if (count == rank){
        return record.get(0);
      }
    }

  }
  return "NO NAME FOUND";
}

public void testGetName(){
  int year = 1980;
  int rank = 350;
  String gender = "F";
  System.out.println("The name that ranked " + rank + " in "+ year + " is "+ getName(year,rank,gender));
  year = 1982;
  rank = 450;
  gender = "M";
  System.out.println("The name that ranked " + rank + " in "+ year + " is "+ getName(year,rank,gender));
}

// #############################################################################

public void whatIsNameInYear(int year, String name, int newYear, String gender){
  // What would your name be if you were born in a different year (Based on populariry)
  int rank = getRank(year, name, gender);
  String newName = getName(newYear, rank, gender);
  if (gender.equals("F")){
    System.out.println( name + " born in " + year + " would be " + newName + " if she was born in " + newYear);
  }
  else {
    System.out.println(name + " born in " + year + " would be " + newName + " if he was born in " + newYear);
  }

}

// ################################################################################

public int yearOfHighestRank(String name, String gender){
  // This method selects a range of files to process and returns an integer,
  // the year with the highest rank for the name and gender.
  int rank = 10000; // arbitrary number that would be bigger than all the ranks
  int year = -1;
  DirectoryResource dr = new DirectoryResource();

  for (File fr : dr.selectedFiles()){
    String yearName = fr.getName().substring(3,7);
    int currYear = Integer.parseInt(yearName);
    int currRank = getRank(currYear, name, gender);
    if (currRank != -1 && currRank < rank){
      rank = currRank;
      year = currYear;
    }
  }
  return year;
}

public void testYearOfHighestRank(){
  String name = "Mich";
  String gender = "M";
  System.out.println("The highest ranking of the name was in " + yearOfHighestRank(name, gender));
}

// #############################################################################


public double getAverageRank(String name, String gender){
  // This method selects a range of files to process and returns a double
  //representing the average rank of the name and gender over the selected files.
  double total = 0;
  int count = 0;
  DirectoryResource dr = new DirectoryResource();
  for (File fr : dr.selectedFiles()){
    String yearName = fr.getName().substring(3,7);
    int currYear = Integer.parseInt(yearName);
    total += getRank(currYear, name, gender);
    count += 1;
  }
  if (total == 0.0){
    return -1.0;
  }
  else {
    return total/count;
  }
}


public void testGetAverageRank(){
  String name = "Robert";
  String gender = "M";
  System.out.println("The average ranking of the name "+ name + " is " + getAverageRank(name,gender));
  gender = "F";
  name = "Susan";
  System.out.println("The average ranking of the name "+ name + " is " + getAverageRank(name,gender));
}

// #############################################################################

public int getTotalBirthsRankedHigher(int year, String name, String gender){
  // This method returns an integer, the total number of births of those names
  // with the same gender and same year who are ranked higher than name.
  int count = 0;
  int currRank = 0; // track down the rank of every name using the order in the file.
  FileResource fr = new FileResource("us_babynames_by_year/yob" + year + ".csv");
  CSVParser parser = fr.getCSVParser(false);
  int rank = getRank(year,name,gender);
  for (CSVRecord record : parser){
    if (record.get(1).equals(gender)){
      currRank += 1;
      if (currRank < rank){
        count += Integer.parseInt(record.get(2));
      }
      else {break;}
    }
  }
  return count;
}

public void testGetTotalBirthsRankedHigher(){
  String name = "Emily";
  String gender = "F";
  int year = 1990;
  System.out.println("There are "+ getTotalBirthsRankedHigher(year,name,gender) + " borns ranking higher");
  name = "Drew";
  gender = "M";
  System.out.println("There are "+ getTotalBirthsRankedHigher(year,name,gender) + " borns ranking higher");
}
// #############################################################################
 public static void main(String[] args){
   BabyBirths b = new BabyBirths();
    //b.printNames();
   //FileResource fr = new FileResource();
   //b.totalbirths(fr);
   // b.testGetRank();
  b.testGetName();
  //b.whatIsNameInYear(1972, "Susan", 2014, "F");
  //b.whatIsNameInYear(1974, "Owen", 2014, "M");
  //b.testYearOfHighestRank();
  //b.testGetAverageRank();
  // b.testGetTotalBirthsRankedHigher();
 }


}
