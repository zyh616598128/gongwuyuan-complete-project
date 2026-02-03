# Gongwuyuan Application - Advanced Question Types Guide

## Overview
This guide describes the advanced question types supported by the Gongwuyuan (公务员考试) application, specifically Material Analysis Questions and Composite Questions.

## Features

### 1. Material Analysis Questions (资料分析题)
- **Structured format**: Parent question with reference material and multiple related child questions
- **Rich content support**: Ability to store lengthy reference materials
- **Hierarchical organization**: Related questions grouped under reference material
- **Interactive display**: Collapsible sections for better readability

### 2. Composite Questions (组合题)
- **Parent-child relationship**: Complex questions with sub-questions
- **Flexible structure**: Support for various combinations of question types
- **Sequential presentation**: Child questions presented in logical order
- **Unified scoring**: Overall score based on individual sub-question performance

## Data Model Extensions

### Enhanced Question Model
The Question entity has been extended with:

#### New Fields
- `referenceMaterial`: Stores reference text for material analysis questions
- `parentQuestion`: Links child questions to parent in composite questions
- `subQuestions`: Collection of child questions
- `sortIndex`: Order index for sub-questions

#### New Question Types
- `MATERIAL_ANALYSIS`: For questions requiring analysis of provided materials
- `COMPOSITE_QUESTION`: For complex questions with multiple parts

## Backend Implementation

### Controllers
- **MaterialAnalysisController**: Handles material analysis question CRUD operations
- **CompositeQuestionController**: Manages composite question creation and retrieval
- **QuestionController**: Extended with child question retrieval methods

### Services
- **QuestionService**: Enhanced with child question management methods
- **Custom Repository Methods**: Added for hierarchical question queries

### Models
- **Extended Question Entity**: With parent-child relationships and reference materials
- **Specialized Request Objects**: For material analysis and composite question creation

## API Endpoints

### Material Analysis Questions
- `POST /api/material-analysis` - Create material analysis question
- `GET /api/material-analysis/{id}` - Get material analysis question with children
- `POST /api/material-analysis/import/zip` - Bulk import from ZIP

### Composite Questions
- `POST /api/composite-questions` - Create composite question
- `GET /api/composite-questions/{id}` - Get composite question with children
- `POST /api/composite-questions/import/json` - Import from JSON

### Child Question Retrieval
- `GET /api/questions/parent/{parentId}/children` - Get all child questions

## Frontend Implementation

### Pages
- **MaterialAnalysisPage**: Dedicated page for material analysis exercises
- **Enhanced PracticePage**: Now supports hierarchical question display
- **AdminImportPage**: Extended with advanced import options

### Features
- **Collapsible Sections**: For viewing reference materials and related questions
- **Hierarchical Navigation**: Easy movement between parent and child questions
- **Rich Text Display**: Proper rendering of long reference materials
- **Interactive Answering**: Ability to answer individual sub-questions

## Data Storage Strategy

### Reference Materials
- Stored as text in the `referenceMaterial` field
- Supports large text content (up to 5000 characters)
- Indexed for quick retrieval
- Associated with parent questions in material analysis sets

### Question Hierarchy
- Parent questions contain reference materials and overall context
- Child questions linked via foreign key relationship
- Sort index maintains order of sub-questions
- Cascading operations ensure data integrity

### File Storage
- ZIP files for bulk material analysis question imports
- JSON files for composite question structures
- CSV files for traditional question imports
- All files processed server-side with validation

## Import Capabilities

### Material Analysis Questions
- ZIP import format supporting reference materials and related questions
- Structured format for organized content
- Validation of material-question relationships

### Composite Questions
- JSON import format with parent-child structure
- Support for complex question arrangements
- Automatic hierarchy validation

### Traditional Questions
- CSV format remains supported
- Enhanced with additional field mappings
- Backward compatibility maintained

## Usage Scenarios

### For Students
- Browse material analysis questions in dedicated section
- Read reference materials and answer related questions
- Navigate between parent materials and child questions
- Track performance on hierarchical question sets

### For Administrators
- Import complex question sets via ZIP/JSON formats
- Organize materials with related questions
- Maintain hierarchical question structures
- Validate question relationships

## Technical Considerations

### Performance
- Caching strategies for hierarchical data retrieval
- Paginated results for large question sets
- Optimized database queries for parent-child relationships

### Scalability
- Modular design allows for additional question types
- Extensible API for future enhancements
- Database indexing for efficient querying

### Security
- Proper authorization for question creation/modification
- Input validation for reference materials
- Sanitization of rich text content

## Future Enhancements
- Support for multimedia reference materials (images, PDFs)
- Advanced formatting for reference materials
- Cross-reference capabilities between questions
- Advanced analytics for hierarchical question performance
- Import from various document formats (DOCX, PDF)

## Integration
- Seamless integration with existing practice and exam systems
- Consistent UI/UX patterns across all question types
- Unified scoring and reporting mechanisms
- Compatible with all existing API endpoints

## Support
Advanced question types have been thoroughly tested with:
- Various content lengths and complexities
- Hierarchical data integrity
- Performance under load
- Cross-browser compatibility
- Mobile responsiveness

This enhancement significantly expands the application's capability to handle complex examination formats typical in civil service examinations.