<?xml version="1.0"?>
<?mso-application progid="Excel.Sheet"?>
<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"
 xmlns:o="urn:schemas-microsoft-com:office:office"
 xmlns:x="urn:schemas-microsoft-com:office:excel"
 xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
 xmlns:html="http://www.w3.org/TR/REC-html40">
 <DocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
  <Created>1996-12-17T01:32:42Z</Created>
  <LastSaved>2012-07-24T07:37:46Z</LastSaved>
  <Version>12.00</Version>
 </DocumentProperties>
 <OfficeDocumentSettings xmlns="urn:schemas-microsoft-com:office:office">
  <RemovePersonalInformation/>
 </OfficeDocumentSettings>
 <ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">
  <WindowHeight>4530</WindowHeight>
  <WindowWidth>8505</WindowWidth>
  <WindowTopX>480</WindowTopX>
  <WindowTopY>120</WindowTopY>
  <ProtectStructure>False</ProtectStructure>
  <ProtectWindows>False</ProtectWindows>
 </ExcelWorkbook>
 <Styles>
  <Style ss:ID="Default" ss:Name="Normal">
   <Alignment ss:Vertical="Bottom"/>
   <Borders/>
   <Font ss:FontName="宋体" x:CharSet="134" ss:Size="12"/>
   <Interior/>
   <NumberFormat/>
   <Protection/>
  </Style>
  <Style ss:ID="s62">
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Font ss:FontName="宋体" x:CharSet="134" ss:Size="12"/>
  </Style>
 </Styles>
 <#list list as sheet>
 <Worksheet ss:Name="${sheet.name}">
  <Table ss:ExpandedColumnCount="16384" ss:ExpandedRowCount="1048576" x:FullColumns="1"
   x:FullRows="1" ss:DefaultColumnWidth="54" ss:DefaultRowHeight="14.25">
   <Column ss:AutoFitWidth="0" ss:Width="15"/>
   <Column ss:AutoFitWidth="0" ss:Width="78.75"/>
   <Column ss:AutoFitWidth="0" ss:Width="182.25"/>
   <Column ss:Index="5" ss:AutoFitWidth="0" ss:Width="87.75"/>
   <Row ss:AutoFitHeight="0"/>
   <Row ss:AutoFitHeight="0">
    <Cell ss:Index="2" ss:StyleID="s62"><Data ss:Type="String">文件夹</Data></Cell>
    <Cell ss:StyleID="s62"><Data ss:Type="String">名称</Data></Cell>
    <Cell ss:StyleID="s62"><Data ss:Type="String">大小</Data></Cell>
    <Cell ss:StyleID="s62"><Data ss:Type="String">创建时间</Data></Cell>
   </Row>
   <#list sheet.catalog as acatalog>
   <#list acatalog.file as file_item>
   <Row ss:AutoFitHeight="0">
    <Cell ss:Index="2" ss:StyleID="s62"><Data ss:Type="String">${acatalog.name}</Data></Cell>
    <Cell ss:StyleID="s62"><Data ss:Type="String">${file_item.name}</Data></Cell>
    <Cell ss:StyleID="s62"><Data ss:Type="String">${file_item.size}</Data></Cell>
    <Cell ss:StyleID="s62"><Data ss:Type="Number">${file_item.key[0..8]}</Data></Cell>
   </Row>
   </#list>
   </#list>   
   <#list sheet.file as afile>
   <Row ss:AutoFitHeight="0">
    <Cell ss:Index="2" ss:StyleID="s62"><Data ss:Type="String"> </Data></Cell>
    <Cell ss:StyleID="s62"><Data ss:Type="String">${afile.name}</Data></Cell>
    <Cell ss:StyleID="s62"><Data ss:Type="String">${afile.size}</Data></Cell>
    <Cell ss:StyleID="s62"><Data ss:Type="Number">${afile.key[0..8]}</Data></Cell>
   </Row>
   </#list> 
  </Table>
  <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
   <Unsynced/>
   <Print>
    <ValidPrinterInfo/>
    <PaperSizeIndex>9</PaperSizeIndex>
    <HorizontalResolution>300</HorizontalResolution>
    <VerticalResolution>300</VerticalResolution>
   </Print>
   <Selected/>
   <Panes>
    <Pane>
     <Number>3</Number>
     <ActiveRow>2</ActiveRow>
     <ActiveCol>1</ActiveCol>
    </Pane>
   </Panes>
   <ProtectObjects>False</ProtectObjects>
   <ProtectScenarios>False</ProtectScenarios>
  </WorksheetOptions>
 </Worksheet>
 </#list>
</Workbook>
