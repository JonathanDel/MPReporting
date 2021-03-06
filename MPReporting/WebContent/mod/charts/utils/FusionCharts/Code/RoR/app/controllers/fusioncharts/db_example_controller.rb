#There are two examples in this controller.
#*Pie-chart for total ouput quantities of each factory by getting data from database and using dataXML method
#*Pie-chart for total ouput quantities of each factory and a link to another chart 
#which gives detailed information for selected factory
#All the views related to this controller will use the "common" layout.
#As per Ruby On Rails conventions, we have the corresponding views 
#with the same name as the function name in the controller.
class Fusioncharts::DbExampleController < ApplicationController
  #This is the layout which all functions in this controller make use of.
  layout "common"
  
  #This action retrieves the values from the database and constructs an array 
  #to hold, factory name and corresponding total output quantity.
  #The view for this action basic_dbexample will use the array values to construct the
  #xml for this chart. To build the xml, the view takes help from the builder file (basic_factories_quantity.builder)
  def basic_dbexample
      headers["content-type"]="text/html";
      @factory_data = [] 
      #Get data from factory masters table
      
      factory_masters = Fusioncharts::FactoryMaster.find(:all)
      factory_masters.each do |factory_master| 
          total = 0.0
          factory_id = factory_master.id
          factory_name = factory_master.name
          factory_master.factory_output_quantities.each do |factory_output|
                  total = total + factory_output.quantity
          end
          # Push the hash of values into the array             
          @factory_data<<{:factory_name=>factory_name,:factory_output=>total}
      end    
  end
  
  #In this function, we obtain total output of quantities and name of each factory from the database and plot them on a pie-chart.
  #It stores URL to the "detailed" function in a variable passing FactoryId as parameter to the function which
  #returns quantity produced and date of production of the factory that are obtained from database and which are plotted in a chart.
  #This action retrieves the values from the database and constructs an array 
  #to hold, factory name, corresponding total output quantity and URL to the action which will generate the detailed chart.
  #The view for this action default.html.erb will use the array values to construct the
  #xml for this chart. To build the xml, the view takes help from the builder file (default_factories_quantity.builder)
  def default
     headers["content-type"]="text/html";
    str_data_url = '';
    @animate_chart = params[:animate]
    if @animate_chart==nil or @animate_chart.empty?
      @animate_chart = '1'
    end
    #Get data from factory masters table
    
    factory_masters = Fusioncharts::FactoryMaster.find(:all)
    
    @factory_data = []
    #Loop through each record
      factory_masters.each do |factory_master| 
          total = 0.0
          #Get factoryid and factoryname 
          factory_id = factory_master.id
          factory_name = factory_master.name
          factory_master.factory_output_quantities.each do |factory_output|
                  total = total + factory_output.quantity
          end
          # Escape the URL 
          str_data_url = "/Fusioncharts/db_example/detailed?"+CGI.escape("FactoryId="+factory_id.to_s)
          # Put the hash of values in the array
          @factory_data<<{:str_data_url=>str_data_url, :factory_name=>factory_name, :factory_output=>total}
      end
  end
  
  #This action retrieves the quantity and date of production of 
  #the factory identified by the request parameter expected "FactoryId"
  #The view for this action is detailed.html.erb and it uses the builder file
  #factory_details.builder to build the xml for the column chart.
  def detailed
      headers["content-type"]="text/html";
      @factory_id = params[:FactoryId]
      @factory_data = []
      
      factory_master = Fusioncharts::FactoryMaster. find(@factory_id)
      factory_master.factory_output_quantities.each do |factory_output|
                      date_of_production = factory_output.date_pro
                      # Formats the date to dd/mm
                      #formatted_date = date_of_production.strftime('%d/%m')
                      # Formats the date to dd/mm without leading zeroes
                      formatted_date = format_date_remove_zeroes(date_of_production)
                      quantity_number = factory_output.quantity
                      @factory_data<<{:date_of_production=>formatted_date,:quantity_number=>quantity_number}
      end
  end
    
end
