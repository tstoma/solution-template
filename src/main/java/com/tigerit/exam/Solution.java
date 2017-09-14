package com.tigerit.exam;


import static com.tigerit.exam.IO.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All of your application logic should be placed inside this class.
 * Remember we will load your application from our custom container.
 * You may add private method inside this class but, make sure your
 * application's execution points start from inside run method.
 */
public class Solution implements Runnable {
    @Override
    public void run() {
        Integer testCase = readLineAsInteger();
        Integer noOfTables = readLineAsInteger();
        String tables[]=new String[noOfTables];
        Map<String,String[]> tableMap=new HashMap<>();
        Map<String,Integer[]> columnMap=new HashMap();
        for(int t=0;t<noOfTables;t++){
            tables[t]=readLine();
            String rowColumn=readLine();
            String[] rowColumnArray=rowColumn.split(" ");
            Integer noOfColumns=Integer.parseInt(rowColumnArray[0]);
            Integer noOfRecords=Integer.parseInt(rowColumnArray[1]);
            String columnNames=readLine();
            String columns[]=columnNames.split(" ");

            for(String column:columns){
                tableMap.put(column,columns);
            }
            String[] rowString=new String[noOfRecords];
            Integer[] columnValue=new Integer[noOfColumns];
            for(int i=0;i<noOfRecords;i++){
                rowString[i] = readLine();
                String[] rowValue=rowString[i].split(" ");
                for(int j=0;j<noOfColumns;j++){
                    if(columnMap.containsKey(columns[j])){
                        columnMap.get(columns[j]) [i]=Integer.parseInt(rowValue[j]);
                    }else{
                        Integer[] columnValueArray=new Integer[noOfColumns];
                        columnValueArray[i]=Integer.parseInt(rowValue[j]);
                        columnMap.put(columns[j],columnValueArray);
                    }
                }

            }
        }
        printLine("Test: "+testCase);
        Integer noOfQueries=readLineAsInteger();
        String[] queries=new String[noOfQueries];
        for(int i=0;i<noOfQueries;i++){
            StringBuffer queryBuffer=new StringBuffer();
            for(int j=0;j<4;j++){
                queryBuffer.append(readLine());
            }
            if(i<noOfQueries-1){
                readLine();
            }

            queryBuffer.toString();
            printResult(queryBuffer.toString(),columnMap,tableMap);
            printLine("");
        }



    }


    public void printResult(String query,Map<String,Integer[]> columnMap,Map<String,String[]> tableMap) {
        String selectString = query.substring(query.indexOf("SELECT") + 7, query.indexOf("FROM"));
        Boolean selectAll=false;
        ArrayList[] selectedColumns=new ArrayList[2];
        String[] condition =getCondition(selectString,query.substring(query.indexOf("ON") + 2, query.length()),selectedColumns,tableMap);
        Boolean columnPrinted=false;
        if (condition != null) {
                Integer[] firstColumn=columnMap.get(condition[0]);
                Integer[] secondColumn=columnMap.get(condition[1]);

                for(int i=0;i<firstColumn.length;i++){
                    for(int j=0;j<secondColumn.length;j++){
                        if(firstColumn[i]==secondColumn[j]){
                            columnPrinted=printResult(selectedColumns,i,j, columnMap,columnPrinted);
                        }

                    }
                }



        }
    }


    public Boolean printResult(ArrayList[] selectedColumns,int firstPosition,int secondPosition,
                            Map<String,Integer[]> columnMap,Boolean columnPrinted){
        StringBuffer columnBuffer=new StringBuffer();
        StringBuffer resultBuffer=new StringBuffer();
        ArrayList tempColumnList=selectedColumns[0];
        for(Object obj:tempColumnList){
            resultBuffer.append( columnMap.get(obj)[firstPosition] +" ");
            columnBuffer.append( obj+" ");
        }
        tempColumnList=selectedColumns[1];
        for(Object obj:tempColumnList){
            resultBuffer.append( columnMap.get(obj)[firstPosition] +" ");
            columnBuffer.append( obj+" ");
        }


        String finalResult=resultBuffer.toString();
        String finalResultColumn=columnBuffer.toString();

        if(!columnPrinted){
            printLine(finalResultColumn);
            columnPrinted=true;
        }

        printLine(finalResult.trim());

        return  columnPrinted;

    }

    public String[] getCondition(String selectString,String conditionString,ArrayList[] selectedColumns,Map<String,String[]> tableMap){
        String condition=conditionString.substring(conditionString.indexOf("ON")+2,conditionString.length());
        String[] parsedCondition = condition.split("=");
        parsedCondition[0] = parsedCondition[0].substring(parsedCondition[0].indexOf(".") + 1, parsedCondition[0].length()).trim();
        parsedCondition[1] = parsedCondition[1].substring(parsedCondition[1].indexOf(".") + 1, parsedCondition[1].length()).trim();
        if (selectString.contains("*")) {
            int i=0;
            for (String parsedString:parsedCondition){
                ArrayList selectedArray=new ArrayList();
                for(String parsedColumn:tableMap.get(parsedString)){

                    selectedArray.add(parsedColumn);

                }
                selectedColumns[i]=new ArrayList();
                selectedColumns[i]=selectedArray;
                i++;
            }
        } else {
           String[] tempSelected = selectString.split(",");

            for(int i=0;i<tempSelected.length;i++){
              String tempColumn=tempSelected[i].substring(tempSelected[i].indexOf(".")+1,tempSelected[i].length());

               for(String string:  tableMap.get(parsedCondition[0])){
                if(tempColumn.equalsIgnoreCase(string))   {
                    if(selectedColumns[0]==null){
                        selectedColumns[0]=new ArrayList();
                    }
                    selectedColumns[0].add(tempColumn);
                }
               }

               for(String string:  tableMap.get(parsedCondition[1])){
                if(tempColumn.equalsIgnoreCase(string))   {
                    if(selectedColumns[1]==null){
                        selectedColumns[1]=new ArrayList();
                    }
                    selectedColumns[1].add(tempColumn);
                }
               }

            }
        }

        return parsedCondition;
    }
}
