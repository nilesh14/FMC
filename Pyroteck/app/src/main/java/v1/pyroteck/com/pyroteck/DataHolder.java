package v1.pyroteck.com.pyroteck;

import java.util.ArrayList;
import java.util.HashMap;

import v1.pyroteck.com.pyroteck.data.Category;
import v1.pyroteck.com.pyroteck.data.SizeData;
import v1.pyroteck.com.pyroteck.data.SubCategory;

/**
 * Created by Nilesh on 01/05/15.
 */
public class DataHolder {
    private static final String XXL = "XXL";
    public static HashMap<String,Category> mapMain = new HashMap<String,Category>();
    public static ArrayList<String> arrCategoryTitle = new ArrayList<String>();
    public static String L = "L";
    public static String XL = "XL";
    public static String S = "S";
    public static String M = "M";
    public static String STANDARD = "Standard";
    public static String CUSTOM_SIZES = "Custom Sizes";
    public static String OS = "OS";
    public static String XOS = "XOS";
    public static String XXOS = "XXOS";
    public static String XOS_3 = "3-XOS";



    public static void prepareData(){

        Category c1 = new Category();
        c1.setTitle("Gloves and Mitts");


        SubCategory sub1c1 = new SubCategory();
        sub1c1.setTitle("K2 Standard Gauntlet Glove ");
        sub1c1.setImageID(R.drawable.glovesandmitts1);
        ArrayList<SizeData> arrSizeData1 = new ArrayList<SizeData>();
        SizeData s1_1 = new SizeData();
        s1_1.setMetricCM(30.5);
        s1_1.setSize(S);
        arrSizeData1.add(s1_1);
        SizeData s1_2 = new SizeData();
        s1_2.setMetricCM(35.6);
        s1_2.setSize(M);
        arrSizeData1.add(s1_2);
        SizeData s1_3 = new SizeData();
        s1_3.setMetricCM(40.6);
        s1_3.setSize(L);
        arrSizeData1.add(s1_3);
        SizeData s1_4 = new SizeData();
        s1_4.setSize(XL);
        s1_4.setMetricCM(43.2);
        arrSizeData1.add(s1_4);

        sub1c1.setShowTable(true);
        sub1c1.setArrSizeData(arrSizeData1);
        sub1c1.setMaterial("Woven aramid palm, knitted aramid back, aramid felt cuff, and wool liner.");
        sub1c1.setDescription("K2 gauntlet gloves are comfortable, offering general purpose protection against possible contact with hot surfaces and occasional metal splash.");
        c1.getArrSubcategory().add(sub1c1);


        SubCategory sub2c1 = new SubCategory();
        sub2c1.setTitle("K2 Aluminized Gauntlet Glove");
        sub2c1.setImageID(R.drawable.glovesandmitts2);
        ArrayList<SizeData> arrSizeData2 = new ArrayList<SizeData>();
        SizeData s2_1 = new SizeData();
        s2_1.setMetricCM(40.6);
        s2_1.setSize(L);
        arrSizeData2.add(s2_1);
        SizeData s2_2 = new SizeData();
        s2_2.setSize(XL);
        s2_2.setMetricCM(43.2);
        arrSizeData2.add(s2_2);
        sub2c1.setArrSizeData(arrSizeData2);
        sub2c1.setShowTable(true);
        sub2c1.setMaterial("Leather or aramid palm, heat-resistant plastic film- coated foil covered aramid back, aramid felt cuff and wool liner.");
        sub2c1.setDescription("Developed for induction furnace operations, K2 aluminized gauntlet gloves provide protection from radiant heat and occasional metal splash while retaining some user tactile properties. Additionally, these gloves have extra reinforcement on the seams to prolong service life.");
        c1.getArrSubcategory().add(sub2c1);

        SubCategory sub3c1 = new SubCategory();
        sub3c1.setTitle("K2 Standard Gauntlet Glove-Long");
        sub3c1.setImageID(R.drawable.glovesandmitts3);
        ArrayList<SizeData> arrSizeData3 = new ArrayList<SizeData>();
        SizeData s3_1 = new SizeData();
        s3_1.setMetricCM(40.6);
        s3_1.setSize(L);
        arrSizeData3.add(s3_1);
        SizeData s3_2 = new SizeData();
        s3_2.setSize(XL);
        s3_2.setMetricCM(43.2);
        arrSizeData3.add(s3_2);
        sub3c1.setArrSizeData(arrSizeData3);
        sub3c1.setShowTable(true);
        sub3c1.setMaterial("Woven aramid palm, knitted aramid back, aramid felt cuff and wool liner.");
        sub3c1.setDescription("Comfortable, general-purpose protection against possible contact with hot surfaces, this standard K2 glove is modified with a long cuff to protect the forearm.");
        c1.getArrSubcategory().add(sub3c1);


        SubCategory sub4c1 = new SubCategory();
        sub4c1.setTitle("K12 Boilerman’s Glove");
        sub4c1.setImageID(R.drawable.glovesandmitts4);
        ArrayList<SizeData> arrSizeData4 = new ArrayList<SizeData>();
        SizeData s4_1 = new SizeData();
        s4_1.setMetricCM(40.6);
        s4_1.setSize(L);
        arrSizeData4.add(s4_1);
        SizeData s4_2 = new SizeData();
        s4_2.setSize(XL);
        s4_2.setMetricCM(43.2);
        arrSizeData4.add(s4_2);
        sub4c1.setArrSizeData(arrSizeData4);
        sub4c1.setShowTable(true);
        sub4c1.setMaterial("Carbon-reinforced aramid overlaying a woven aramid palm, knitted aramid back, aramid felt cuff and wool liner");
        sub4c1.setDescription("Providing extra-tough protection for working around the boiler firebox, K12 gloves provide good protection from radiant and applied heat. These gloves have extra reinforcement on the seams and durable materials for prolonged service life.");
        c1.getArrSubcategory().add(sub4c1);


        SubCategory sub5c1 = new SubCategory();
        sub5c1.setTitle("K12 Standard Gauntlet Glove");
        sub5c1.setImageID(R.drawable.glovesandmitts5);
        ArrayList<SizeData> arrSizeData5 = new ArrayList<SizeData>();
        SizeData s5_1 = new SizeData();
        s5_1.setMetricCM(30.5);
        s5_1.setSize(S);
        arrSizeData5.add(s5_1);
        SizeData s5_2 = new SizeData();
        s5_2.setMetricCM(40.6);
        s5_2.setSize(L);
        arrSizeData5.add(s5_2);
        SizeData s5_3 = new SizeData();
        s5_3.setSize(XL);
        s5_3.setMetricCM(43.2);
        arrSizeData5.add(s5_3);
        sub5c1.setArrSizeData(arrSizeData5);
        sub5c1.setShowTable(true);
        sub5c1.setMaterial("Woven aramid palm, knitted aramid back, aramid felt cuff,extra insulation and wool liner.");
        sub5c1.setDescription("Designed for heavy-duty, high-temperature tasks, K12 gauntlet gloves provide excellent protection from applied heat and occasional metal splash. These gloves have extra reinforcement on the seams to prolong service life against tool abrasion.");
        c1.getArrSubcategory().add(sub5c1);


        SubCategory sub6c1 = new SubCategory();
        sub6c1.setTitle("K15 Welding Mitt");
        sub6c1.setImageID(R.drawable.glovesandmitts6);
        ArrayList<SizeData> arrSizeData6 = new ArrayList<SizeData>();
        SizeData s6_1 = new SizeData();
        s6_1.setSize(STANDARD);
        arrSizeData6.add(s6_1);
        sub6c1.setArrSizeData(arrSizeData6);
        sub6c1.setShowTable(false);
        sub6c1.setMaterial("Woven aramid palm, knitted aramid back, aramid felt cuff,extra insulation and wool liner.");
        sub6c1.setDescription("Designed for heavy-duty, high-temperature tasks, K12 gauntlet gloves provide excellent protection from applied heat and occasional metal splash.");
        c1.getArrSubcategory().add(sub6c1);


        SubCategory sub7c1 = new SubCategory();
        sub7c1.setTitle("K5 Mitt");
        sub7c1.setImageID(R.drawable.glovesandmitts7);
        ArrayList<SizeData> arrSizeData7 = new ArrayList<SizeData>();
        SizeData s7_1 = new SizeData();
        s7_1.setSize(STANDARD);
        arrSizeData7.add(s7_1);
        sub7c1.setArrSizeData(arrSizeData7);
        sub7c1.setShowTable(false);
        sub7c1.setMaterial("Woven aramid, front and back, without a liner.");
        sub7c1.setDescription("The K5 mitt was designed as a sacrificial over- mitt to be worn over other gloves to extend service life.\n" +
                "The K5 mitt has proven suitable for use in stand-alone applications, such as a baker's mitt, handling hot trays or bread pans.\n");
        c1.getArrSubcategory().add(sub7c1);

        SubCategory sub8c1 = new SubCategory();
        sub8c1.setTitle("Aramid Knitted Gloves");
        sub8c1.setImageID(R.drawable.glovesandmitts9);
        ArrayList<SizeData> arrSizeData8 = new ArrayList<SizeData>();
        SizeData s8_1 = new SizeData();
        s8_1.setSize(STANDARD);
        arrSizeData8.add(s8_1);
        sub8c1.setArrSizeData(arrSizeData8);
        sub8c1.setShowTable(false);
        sub8c1.setMaterial("Knitted aramid yarn.");
        sub8c1.setDescription("Thin and comfortable, these gloves provide basic protection against heat, cuts and abrasion while providing flexibility, good tactile response and mobility.");
        c1.getArrSubcategory().add(sub8c1);

        SubCategory sub9c1 = new SubCategory();
        sub9c1.setTitle("K7 Mitt");
        sub9c1.setImageID(R.drawable.glovesandmitts8);
        ArrayList<SizeData> arrSizeData9 = new ArrayList<SizeData>();
        SizeData s9_1 = new SizeData();
        s9_1.setSize(STANDARD);
        arrSizeData9.add(s9_1);
        sub9c1.setArrSizeData(arrSizeData9);
        sub9c1.setShowTable(false);
        sub9c1.setMaterial("Woven aramid, front and back with a double wool liner. Also available in woven carbon-reinforced aramid.");
        sub9c1.setDescription("A heavy-duty mitt for high-temperature applications with no tactile requirement, the K7 is used in aluminium extrusion plants and casting pits to move newly formed products or hot tooling.");
        c1.getArrSubcategory().add(sub9c1);

        mapMain.put("Gloves and Mitts", c1);
        arrCategoryTitle.add("Gloves and Mitts");

        // Category 2
        Category c2 = new Category();
        c2.setTitle("Foot Protection");

        SubCategory sub1c2 = new SubCategory();
        sub1c2.setTitle("Aluminized Aramid Spat");
        sub1c2.setImageID(R.drawable.footprotection);
        ArrayList<SizeData> arrSizeDataC2_1 = new ArrayList<SizeData>();
        SizeData s1_C2 = new SizeData();
        s1_C2.setSize(CUSTOM_SIZES);
        arrSizeDataC2_1.add(s1_C2);
        sub1c2.setArrSizeData(arrSizeDataC2_1);
        sub1c2.setShowTable(false);
        sub1c2.setMaterial("Aluminized aramid fabric.");
        sub1c2.setDescription("Using high quality materials, aluminized spats are designed to customer requirements and provide excellent protection against heat and molten metal splash.");
        c2.getArrSubcategory().add(sub1c2);

        mapMain.put("Foot Protection",c2);

        arrCategoryTitle.add("Foot Protection");

        //Category 3

        Category c3 = new Category();
        c3.setTitle("Leggings");

        SubCategory sub1c3 = new SubCategory();
        sub1c3.setTitle("Reflective Leggings");
        sub1c3.setImageID(R.drawable.leggings);
        ArrayList<SizeData> arrSizeDataC3_1 = new ArrayList<SizeData>();
        SizeData s1_C3 = new SizeData();
        s1_C3.setSize(L);
        s1_C3.setMetricCM(40.6);
        arrSizeDataC3_1.add(s1_C3);
        sub1c3.setArrSizeData(arrSizeDataC3_1);
        sub1c3.setShowTable(true);
        sub1c3.setMaterial("Heat-resistant plastic film-coated foil covering oxidized carbon-fibre aramid.");
        sub1c3.setDescription("These leggings are designed for both foundry and primary smelter operations where an operator is in close proximity to the radiant heat from reduction cells, holding furnaces or involved in ladle pouring and casting operations. " +
                "These leggings provide protection against radiant heat and molten metal splash. The fastenings behind each leg are made from a special heat-resistant material approved by the U.S. Federal Aviation Authority (FAA). " +
                "The fasteners are supplied at a generous length and can be trimmed to fit. In addition, the leggings feature a strong synthetic adjustable waist belt with a plastic buckle.");
        c3.getArrSubcategory().add(sub1c3);

        mapMain.put("Leggings",c3);

        arrCategoryTitle.add("Leggings");


        //Category 4

        Category c4 = new Category();
        c4.setTitle("Hoods");

        SubCategory sub1c4 = new SubCategory();
        sub1c4.setTitle("Anti-Flash / Welding");
        sub1c4.setImageID(R.drawable.hoods1);
        ArrayList<SizeData> arrSizeDataC4_1 = new ArrayList<SizeData>();
        SizeData s1_C4 = new SizeData();
        s1_C4.setSize(STANDARD);
        arrSizeDataC4_1.add(s1_C4);
        sub1c4.setArrSizeData(arrSizeDataC4_1);
        sub1c4.setShowTable(false);
        sub1c4.setMaterial("Unique carbon fibre knitted fabric.");
        sub1c4.setDescription("Light and comfortable in confined spaces, the hood protects the head, neck and shoulders from welding operations. Made from materials specified by the NATO Defence Alliance, this hood is manufactured in the style of the naval gunners' anti-flash hood.");
        c4.getArrSubcategory().add(sub1c4);


        SubCategory sub2c4 = new SubCategory();
        sub2c4.setTitle("Helmet Mounting");
        sub2c4.setImageID(R.drawable.hoods2);
        ArrayList<SizeData> arrSizeDataC4_2 = new ArrayList<SizeData>();
        SizeData s2_C4 = new SizeData();
        s2_C4.setSize(STANDARD);
        arrSizeDataC4_2.add(s2_C4);
        sub2c4.setArrSizeData(arrSizeDataC4_2);
        sub2c4.setShowTable(false);
        sub2c4.setMaterial("Wool-viscose blend fabric.");
        sub2c4.setDescription("The hood protects the head and neck from radiant heat and metal splash with added shop floor safety from the material's high-visibility colour. This hood attaches to the headband of a protective helmet with heat-resistant fasteners and fastens under the chin if required.");
        c4.getArrSubcategory().add(sub2c4);

        mapMain.put("Hoods", c4);

        arrCategoryTitle.add("Hoods");



        //Category 5

        Category c5 = new Category();
        c5.setTitle("Jackets");

        SubCategory sub1c5 = new SubCategory();
        sub1c5.setTitle("Furnace Reflective Jacket");
        sub1c5.setImageID(R.drawable.jackets1);
        ArrayList<SizeData> arrSizeDataC5_1 = new ArrayList<SizeData>();
        SizeData s1_C5 = new SizeData();
        s1_C5.setSize(OS);
        arrSizeDataC5_1.add(s1_C5);
        SizeData s2_C5 = new SizeData();
        s2_C5.setSize(XOS);
        arrSizeDataC5_1.add(s2_C5);
        SizeData s3_C5 = new SizeData();
        s3_C5.setSize(XXOS);
        arrSizeDataC5_1.add(s3_C5);
        SizeData s4_C5 = new SizeData();
        s4_C5.setSize(XOS_3);
        arrSizeDataC5_1.add(s4_C5);
        sub1c5.setArrSizeData(arrSizeDataC5_1);
        sub1c5.setShowTable(false);
        sub1c5.setMaterial("Heat-resistant plastic film coated foil over oxidized carbon fibre aramid and wool viscose blend fabric.");
        sub1c5.setDescription("These reflective casting jackets are used as additional protection over standard work wear when doing furnace work or in any operation where radiant heat is an issue. The fabric also provides protection against molten metal splash. The back panel and underarm gusset allow for air movement and greater flexibility. The removable front \"bib\" stops heat reflecting under the visor and onto the face and neck.");
        c5.getArrSubcategory().add(sub1c5);

        SubCategory sub2c5 = new SubCategory();
        sub2c5.setTitle("Casting Jacket");
        sub2c5.setImageID(R.drawable.jackets2);
        ArrayList<SizeData> arrSizeDataC5_2 = new ArrayList<SizeData>();
        SizeData s25_C5 = new SizeData();
        s25_C5.setSize(S);
        arrSizeDataC5_2.add(s25_C5);
        SizeData s26_C5 = new SizeData();
        s26_C5.setSize(M);
        arrSizeDataC5_2.add(s26_C5);
        SizeData s21_C5 = new SizeData();
        s21_C5.setSize(OS);
        arrSizeDataC5_2.add(s21_C5);
        SizeData s22_C5 = new SizeData();
        s22_C5.setSize(XOS);
        arrSizeDataC5_2.add(s22_C5);
        SizeData s23_C5 = new SizeData();
        s23_C5.setSize(XXOS);
        arrSizeDataC5_2.add(s23_C5);
        SizeData s24_C5 = new SizeData();
        s24_C5.setSize(XOS_3);
        arrSizeDataC5_2.add(s24_C5);
        sub2c5.setArrSizeData(arrSizeDataC5_2);
        sub2c5.setShowTable(false);
        sub2c5.setMaterial("Wool-viscose blend fabric.");
        sub2c5.setDescription("These casting jackets are used as additional protection over the standard work wear at cast start and cast termination.The colour conforms to the high-visibility requirement of AS / NZS 1906 and other international visibility standards.To improve nighttime visibility, 10.2 x 10.2 cm, high-visibility materials for safety garments and two reflective flashes are sewn onto the shoulders.");
        c5.getArrSubcategory().add(sub2c5);


        SubCategory sub3c5 = new SubCategory();
        sub3c5.setTitle("Boiler Suit");
        sub3c5.setImageID(R.drawable.jackets3);
        ArrayList<SizeData> arrSizeDataC5_3 = new ArrayList<SizeData>();
        SizeData s31_C5 = new SizeData();
        s31_C5.setSize(S);
        arrSizeDataC5_3.add(s31_C5);
        SizeData s32_C5 = new SizeData();
        s32_C5.setSize(M);
        arrSizeDataC5_3.add(s32_C5);
        SizeData s33_C5 = new SizeData();
        s33_C5.setSize(L);
        arrSizeDataC5_3.add(s33_C5);
        SizeData s34_C5 = new SizeData();
        s34_C5.setSize(XL);
        arrSizeDataC5_3.add(s34_C5);
        sub3c5.setArrSizeData(arrSizeDataC5_3);
        sub3c5.setShowTable(false);
        sub3c5.setMaterial("Flame retardant woven cotton fabric, para-aramid thread.");
        sub3c5.setDescription("Made for general protective use in high- temperature environments, boiler suits are manufactured in two main types: heat resistant and heat and electric arc resistant.");
        c5.getArrSubcategory().add(sub3c5);


        SubCategory sub4c5 = new SubCategory();
        sub4c5.setTitle("Weld Spat Protection Jacket");
        sub4c5.setImageID(R.drawable.jackets4);
        ArrayList<SizeData> arrSizeDataC5_4 = new ArrayList<SizeData>();
        SizeData s41_C5 = new SizeData();
        s41_C5.setSize(S);
        arrSizeDataC5_4.add(s41_C5);
        SizeData s42_C5 = new SizeData();
        s42_C5.setSize(M);
        arrSizeDataC5_4.add(s42_C5);
        SizeData s43_C5 = new SizeData();
        s43_C5.setSize(L);
        arrSizeDataC5_4.add(s43_C5);
        sub4c5.setArrSizeData(arrSizeDataC5_4);
        sub4c5.setShowTable(false);
        sub4c5.setMaterial("Outer layer of pre- oxidized acrylic / carbon reinforced aramid aluminized fabric.");
        sub4c5.setDescription("The welding jacket is designed to help protect operators from sparks, spatter and slag generated by welding or other metal-cutting applications.");
        c5.getArrSubcategory().add(sub4c5);

        mapMain.put("Jackets",c5);

        arrCategoryTitle.add("Jackets");


        //Category 6

        Category c6 = new Category();
        c6.setTitle("Blankets");

        SubCategory sub1c6 = new SubCategory();
        sub1c6.setTitle("Pyroweld Curtains");
        sub1c6.setImageID(R.drawable.blankets1);
        ArrayList<SizeData> arrSizeDataC6_1 = new ArrayList<SizeData>();
        SizeData s1_C6 = new SizeData();
        s1_C6.setSize("100 x 100 cm");
        arrSizeDataC6_1.add(s1_C6);
        sub1c6.setArrSizeData(arrSizeDataC6_1);
        sub1c6.setShowTable(false);
        sub1c6.setMaterial("Textured fibreglass fabric.");
        sub1c6.setDescription("The welding blanket is designed to protect operators from sparks, spatter and slag generated by welding or other metal-cutting applications. Pyroweld curtains are also available in custom sizes.");
        c6.getArrSubcategory().add(sub1c6);


        SubCategory sub2c6 = new SubCategory();
        sub2c6.setTitle("Fire Blanket");
        sub2c6.setImageID(R.drawable.blankets2);
        ArrayList<SizeData> arrSizeDataC6_2 = new ArrayList<SizeData>();
        SizeData s21_C6 = new SizeData();
        s21_C6.setSize(CUSTOM_SIZES);
        arrSizeDataC6_2.add(s21_C6);
        sub2c6.setArrSizeData(arrSizeDataC6_2);
        sub2c6.setShowTable(false);
        sub2c6.setMaterial("Continuous filament glass fibre.");
        sub2c6.setDescription("Used for urgent response to smother and stop small accidental fires, these blankets are lightweight and can resist temperatures up to 500°C.");
        c6.getArrSubcategory().add(sub2c6);

        mapMain.put("Blankets",c6);

        arrCategoryTitle.add("Blankets");

        // Category 7
        Category c7 = new Category();
        c7.setTitle("Full Suits");

        SubCategory sub1c7 = new SubCategory();
        sub1c7.setTitle("Fire Proximity Suit");
        sub1c7.setImageID(R.drawable.fullsuits);
        ArrayList<SizeData> arrSizeDataC7_1 = new ArrayList<SizeData>();
        SizeData s1_C7 = new SizeData();
        s1_C7.setSize(STANDARD);
        arrSizeDataC7_1.add(s1_C7);
        sub1c7.setArrSizeData(arrSizeDataC7_1);
        sub1c7.setShowTable(false);
        sub1c7.setMaterial("Outer layer of aluminized fibreglass.");
        sub1c7.setDescription("The fire proximity suit consists of a one-piece coverall, or pant and shirt; a hood with visor and built-in helmet; a pair of five-finger gloves and a pair of customized protective boots.");
        c7.getArrSubcategory().add(sub1c7);

        mapMain.put("Full Suits",c7);

        arrCategoryTitle.add("Full Suits");

        // Category 8
        Category c8 = new Category();
        c8.setTitle("Apron");

        SubCategory sub1c8 = new SubCategory();
        sub1c8.setTitle("Weld Spat Protection K Type Apron");
        sub1c8.setImageID(R.drawable.apron);
        ArrayList<SizeData> arrSizeDataC8_1 = new ArrayList<SizeData>();
        SizeData s11_C8 = new SizeData();
        s11_C8.setSize(S);
        s11_C8.setCustomeSizes("81.3 x 40.6");
        arrSizeDataC8_1.add(s11_C8);
        SizeData s12_C8 = new SizeData();
        s12_C8.setSize(M);
        s12_C8.setCustomeSizes("86.4 x 43.2");
        arrSizeDataC8_1.add(s12_C8);
        SizeData s13_C8 = new SizeData();
        s13_C8.setSize(L);
        s13_C8.setCustomeSizes("91.4 x 45.7");
        arrSizeDataC8_1.add(s13_C8);
        SizeData s14_C8 = new SizeData();
        s14_C8.setSize(XL);
        s14_C8.setCustomeSizes("96.5 x 48.3");
        arrSizeDataC8_1.add(s14_C8);
        SizeData s15_C8 = new SizeData();
        s15_C8.setSize(XXL);
        s15_C8.setCustomeSizes("101.6 x 50.8");
        arrSizeDataC8_1.add(s15_C8);
        sub1c8.setArrSizeData(arrSizeDataC8_1);
        sub1c8.setCheckCustomeSize(true);
        sub1c8.setShowTable(true);
        sub1c8.setMaterial("Outer layer of pre-oxidized acrylic/carbon reinforced aramid aluminized fabric.");
        sub1c8.setDescription("The lightweight, low-cost welding apron covers the front portion of the body with fabric that has been tested to ISO standards for resistance to convective, " +
                "radiant and contact heat, molten iron splash, flame resistance and tear strength.");
        c8.getArrSubcategory().add(sub1c8);

        mapMain.put("Apron",c8);

        arrCategoryTitle.add("Apron");

        // Category 9
        Category c9 = new Category();
        c9.setTitle("Secondary Protection");

        SubCategory sub1c9 = new SubCategory();
        sub1c9.setTitle("Molten Metal Resistant Clothing");
        sub1c9.setImageID(R.drawable.secondary_protection);
        /*ArrayList<SizeData> arrSizeDataC9_1 = new ArrayList<SizeData>();
        SizeData s1_C9 = new SizeData();
        s1_C9.setSize(STANDARD);
        arrSizeDataC9_1.add(s1_C9);
        sub1c9.setArrSizeData(arrSizeDataC9_1);*/
        sub1c9.setAvailableColors("Light blue, navy blue, orange, black");
        sub1c9.setAvailableForms("Shirts, trousers, coveralls, jackets");
        sub1c9.setShowTable(false);
        sub1c9.setMaterial("Wool and viscose blend.");
        sub1c9.setDescription("Used for secondary protection, this material allows for wearer comfort and durability while providing metal shedding and flame resistance. " +
                "Machine washable, the fabric also protects from radiant and convective heat, flame and electric arc hazards. I" +
                "t can be used as secondary protection from most molten metals including aluminium, cryolite, iron, copper, magnesium and nickel.");
        c9.getArrSubcategory().add(sub1c9);

        mapMain.put("Secondary Protection",c9);

        arrCategoryTitle.add("Secondary Protection");

    }
}
