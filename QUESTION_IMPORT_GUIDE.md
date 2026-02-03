# Gongwuyuan Application - Question Import Guide

## Overview
This guide describes the question import functionality for the Gongwuyuan (公务员考试) application. Administrators can use this feature to bulk import questions from CSV files.

## Features

### 1. CSV Import Functionality
- **Bulk import**: Import multiple questions at once
- **CSV format support**: Standard comma-separated values format
- **Progress tracking**: Real-time upload progress indicator
- **Error reporting**: Detailed error messages for invalid records
- **Validation**: Data validation during import process

### 2. Data Fields Supported
The CSV file should contain the following columns in order:

1. **题目内容** (Question Content) - The main question text
2. **选项A** (Option A) - First option
3. **选项B** (Option B) - Second option
4. **选项C** (Option C) - Third option
5. **选项D** (Option D) - Fourth option
6. **答案** (Answer) - Correct answer (e.g., "A", "A,B" for multiple choice)
7. **难度** (Difficulty) - Difficulty level: EASY, MEDIUM, HARD

### 3. API Endpoint
- **Endpoint**: `POST /api/import/questions/csv`
- **Parameters**:
  - `file`: Multipart file upload (CSV format)
  - `subjectId`: Subject ID to associate questions with

### 4. Sample CSV Format
```csv
"题目内容","选项A","选项B","选项C","选项D","答案","难度"
"中国共产党的宗旨是什么？","为人民服务","为国家服务","为社会服务","为人民利益服务","A","MEDIUM"
"中国特色社会主义最本质的特征是什么？","党的领导","人民当家作主","依法治国","共同富裕","A","MEDIUM"
```

## Backend Implementation

### Controllers
- **ImportController**: Handles CSV import functionality
- **QuestionController**: Manages question CRUD operations

### Services
- **QuestionService**: Provides business logic for question management
- **CsvParser**: Utility class for parsing CSV files correctly

### Models
- **Question**: Entity representing a question with all attributes
- **Subject**: Entity representing question subject categories

## Frontend Implementation

### Pages
- **AdminImportPage**: Administrative interface for importing questions

### Features
- File selection interface
- Subject selection dropdown
- Progress tracking during upload
- Detailed result reporting
- Error handling and validation

## Usage Instructions

### 1. Prepare CSV File
- Format your questions according to the specified column order
- Ensure proper CSV formatting (comma-separated, quoted values)
- Include a header row as the first line

### 2. Access Import Interface
- Log in to the application
- Navigate to "题库导入" (Question Import) menu item
- Select the target subject for imported questions
- Choose your CSV file

### 3. Execute Import
- Click "开始导入" (Start Import)
- Monitor progress bar during upload
- Review results after completion

### 4. Verify Results
- Check imported questions in the practice section
- Verify question content and answers
- Confirm proper categorization by subject

## Error Handling

### Common Issues
- **Invalid file format**: Ensure file is in CSV format
- **Missing columns**: Verify all required columns are present
- **Incorrect data types**: Ensure difficulty is one of EASY, MEDIUM, HARD
- **File size limits**: Large files may need to be split

### Error Reporting
- Detailed error messages for each problematic record
- Count of successfully imported vs failed records
- Specific line numbers for troubleshooting

## Security Considerations
- Only authenticated users can access import functionality
- Input validation to prevent malicious data
- Proper authorization checks

## Performance
- Batch processing for efficient import
- Progress tracking for large datasets
- Memory-efficient streaming for large files

## Future Enhancements
- Support for Excel (.xlsx) files
- Advanced validation rules
- Import templates and samples
- Scheduled import functionality
- Import history and audit trail

## Testing
The import functionality has been tested with:
- Various CSV formats
- Edge cases and error conditions
- Large datasets
- Invalid data scenarios

## Support
For issues with question import:
1. Verify CSV format matches specification
2. Check network connectivity
3. Ensure sufficient permissions
4. Review error messages for specific issues