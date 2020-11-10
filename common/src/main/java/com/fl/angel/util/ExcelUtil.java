package com.fl.angel.util;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author fdm
 * @Date 2020/11/10 16:54
 * @description：
 */
public class ExcelUtil {
    private static ByteArrayOutputStream outputStream;
    private static List<String> sheetName;
    private static List<List<? extends BaseRowModel>> datas;
    private static List<Class<? extends BaseRowModel>> clazzs;
    private static ExcelTypeEnum excelTypeEnum;

    /**
     * StringList 解析监听器
     */
    private static class StringExcelListener extends AnalysisEventListener {
        /**
         * 自定义用于暂时存储data
         * 可以通过实例获取该值
         */
        private List<List<String>> datas = new ArrayList<List<String>>();

        /**
         * 每解析一行都会回调invoke()方法
         *
         * @param object
         * @param context
         */
        @Override
        public void invoke(Object object, AnalysisContext context) {
            List<String> stringList= (List<String>) object;
            //数据存储到list，供批量处理，或后续自己业务逻辑处理。
            datas.add(stringList);
            //根据自己业务做处理
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            //解析结束销毁不用的资源
            //注意不要调用datas.clear(),否则getDatas为null
        }

        public List<List<String>> getDatas() {
            return datas;
        }
        public void setDatas(List<List<String>> datas) {
            this.datas = datas;
        }
    }

    /**
     * 模型解析监听器 -- 每解析一行会回调invoke()方法，整个excel解析结束会执行doAfterAllAnalysed()方法
     */
    private static class ModelExcelListener<E> extends AnalysisEventListener<E> {
        private List<E> dataList = new ArrayList<E>();

        @Override
        public void invoke(E object, AnalysisContext context) {
            dataList.add(object);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
        }

        public List<E> getDataList() {
            return dataList;
        }

        @SuppressWarnings("unused")
        public void setDataList(List<E> dataList) {
            this.dataList = dataList;
        }
    }

    /**
     * 使用 StringList 来读取Excel
     * @param inputStream Excel的输入流
     * @param excelTypeEnum Excel的格式(XLS或XLSX)
     * @return 返回 StringList 的列表
     */
    public static List<List<String>> readExcel(InputStream inputStream, ExcelTypeEnum excelTypeEnum) {
        StringExcelListener listener = new StringExcelListener();
        ExcelReader excelReader = new ExcelReader(inputStream, excelTypeEnum, null, listener);
        excelReader.read();
        return  listener.getDatas();
    }

    /**
     * 使用 模型 来读取Excel
     * @param inputStream Excel的输入流
     * @param clazz 模型的类
     * @param excelTypeEnum Excel的格式(XLS或XLSX)
     * @return 返回 模型 的列表
     */
    public static <E> List<E> readExcel(InputStream inputStream, Class<? extends BaseRowModel> clazz, ExcelTypeEnum excelTypeEnum) {
        // 解析每行结果在listener中处理
        ModelExcelListener<E> listener = new ModelExcelListener<E>();
        ExcelReader excelReader = new ExcelReader(inputStream, excelTypeEnum, null, listener);
        //默认只有一列表头
        excelReader.read(new Sheet(1, 1, clazz));
        return listener.getDataList();
    }

    /**
     * 使用 StringList 来写入Excel，单sheet，单table
     * @param outputStream Excel的输出流
     * @param data 要写入的以StringList为单位的数据
     * @param table 配置Excel的表的属性
     * @param excelTypeEnum Excel的格式(XLS或XLSX)
     */
    public static void writeExcel(OutputStream outputStream, List<List<String>> data, Table table, ExcelTypeEnum excelTypeEnum) {
        //这里指定不需要表头，因为String通常表头已被包含在data里
        ExcelWriter writer = new ExcelWriter(outputStream, excelTypeEnum,false);
        //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系,无表头
        Sheet sheet1 = new Sheet(0, 0);
        writer.write0(data, sheet1,table);
        writer.finish();
    }


    /**
     * 使用 StringList 来写入Excel，单sheet，单table（返回byte数组）
     * @param outputStream Excel的输出流
     * @param data 要写入的以StringList为单位的数据
     * @param table 配置Excel的表的属性
     * @param excelTypeEnum Excel的格式(XLS或XLSX)
     * @return   excel字节数组
     */
    public static byte[] writeExcel(ByteArrayOutputStream outputStream, List<List<String>> data, Table table, ExcelTypeEnum excelTypeEnum) {
        //这里指定不需要表头，因为String通常表头已被包含在data里
        ExcelWriter writer = new ExcelWriter(outputStream, excelTypeEnum,false);
        //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系,无表头
        Sheet sheet1 = new Sheet(0, 0);
        writer.write0(data, sheet1,table);
        writer.finish();
        return  outputStream.toByteArray();
    }

    /**
     * 使用 模型 来写入Excel，单sheet，单table
     * @param outputStream Excel的输出流
     * @param data 要写入的以 模型 为单位的数据
     * @param clazz 模型的类
     * @param excelTypeEnum Excel的格式(XLS或XLSX)
     */
    public static void writeExcel(OutputStream outputStream, List<? extends BaseRowModel> data,
                                  Class<? extends BaseRowModel> clazz, ExcelTypeEnum excelTypeEnum)  {
        //这里指定需要表头，因为model通常包含表头信息
        ExcelWriter writer = new ExcelWriter(outputStream, excelTypeEnum,true);
        //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
        Sheet sheet1 = new Sheet(1, 0, clazz);
        writer.write(data, sheet1);
        writer.finish();
    }

    /**
     * 使用 模型 来写入Excel，单sheet，单table （返回字节数组）
     * @param outputStream Excel的输出流
     * @param data 要写入的以 模型 为单位的数据
     * @param clazz 模型的类
     * @param excelTypeEnum Excel的格式(XLS或XLSX)
     * @return excel字节数组
     */
    public static byte[] writeExcel(ByteArrayOutputStream outputStream, List<? extends BaseRowModel> data,
                                    Class<? extends BaseRowModel> clazz, ExcelTypeEnum excelTypeEnum)  {
        //这里指定需要表头，因为model通常包含表头信息
        ExcelWriter writer = new ExcelWriter(outputStream, excelTypeEnum,true);
        //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
        Sheet sheet1 = new Sheet(1, 0, clazz);
        writer.write(data, sheet1);
        writer.finish();
        return outputStream.toByteArray();
    }

    /**
     *使用 模型 来写入Excel，多sheet，单table （返回字节数组）
     * @param outputStream Excel的输出流
     * @param sheetName  sheet名集合
     * @param datas  要写入的以 模型 为单位的数据
     * @param clazzs  模型的类
     * @param excelTypeEnum  Excel的格式(XLS或XLSX)
     * @return
     */
    public static byte[] writeExcel(ByteArrayOutputStream outputStream, List<String> sheetName, List<List<? extends BaseRowModel>> datas,
                                    List<Class<? extends BaseRowModel>> clazzs, ExcelTypeEnum excelTypeEnum)  {
        ExcelUtil.outputStream = outputStream;
        ExcelUtil.sheetName = sheetName;
        ExcelUtil.datas = datas;
        ExcelUtil.clazzs = clazzs;
        ExcelUtil.excelTypeEnum = excelTypeEnum;
        //这里指定需要表头，因为model通常包含表头信息
        ExcelWriter writer = new ExcelWriter(outputStream, excelTypeEnum,true);
        if (sheetName.size()!=datas.size()||datas.size()!=clazzs.size()){
            throw new ArrayIndexOutOfBoundsException();
        }
        int i = 0;
        //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
        for (String name:sheetName){
            Sheet sheet1 = new Sheet(1, 0, clazzs.get(i));
            sheet1.setSheetName(name);
            writer.write(datas.get(i), sheet1);
        }
        writer.finish();
        return outputStream.toByteArray();
    }

    /**
     *  使用 模型 来写入Excel，多sheet，多table
     * @param outputStream  Excel的输出流
     * @param sheetAndTable sheet和table名，格式：<sheet名，<table名集合>>
     * @param data   <sheet名，<table名，table数据集>>
     * @param clazz  <sheet名，<table名，table数据集实体class类型>>
     * @param excelTypeEnum Excel的格式(XLS或XLSX)
     * @return excel字节数组
     */
    public static byte[] writeExcel(ByteArrayOutputStream outputStream, Map<String,List<String>> sheetAndTable,
                                    Map<String,Map<String,List<? extends BaseRowModel>>> data, Map<String,Map<String,Class<? extends BaseRowModel>>> clazz,
                                    ExcelTypeEnum excelTypeEnum){

        //这里指定需要表头，因为model通常包含表头信息
        ExcelWriter writer = new ExcelWriter(outputStream, excelTypeEnum,true);

        Iterator<Map.Entry<String, List<String>>> iterator = sheetAndTable.entrySet().iterator();
        int sheetNo = 1;
        //遍历sheet
        while (iterator.hasNext()){
            Map.Entry<String, List<String>> next = iterator.next();
            //当前sheet名
            String sheetName = next.getKey();
            //当前sheet对应的table的实体类class对象集合
            Map<String, Class<? extends BaseRowModel>> tableClasses = clazz.get(sheetName);
            //当前sheet对应的table的数据集合
            Map<String, List<? extends BaseRowModel>> dataListMaps = data.get(sheetName);
            Sheet sheet = new Sheet(sheetNo, 0);
            sheet.setSheetName(sheetName);
            int tableNo = 1;
            Iterator<Map.Entry<String, Class<? extends BaseRowModel>>> iterator1 = tableClasses.entrySet().iterator();
            //遍历table
            while (iterator1.hasNext()){
                Map.Entry<String, Class<? extends BaseRowModel>> next1 = iterator1.next();
                //当前table名
                String tableName = next1.getKey();
                //当前table对应的class
                Class<? extends BaseRowModel> tableClass = next1.getValue();
                //当前table对应的数据集
                List<? extends BaseRowModel> tableData = dataListMaps.get(tableName);
                Table table = new Table(tableNo);
                table.setClazz(tableClass);
                writer.write(tableData, sheet, table);
                tableNo++;
            }
            sheetNo++;
        }
        writer.finish();
        return  outputStream.toByteArray();

    }
}
