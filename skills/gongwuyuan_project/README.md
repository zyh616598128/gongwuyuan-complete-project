# Gongwuyuan Project Skill

## Overview
This skill provides structured project management for the Gongwuyuan (公务员考试备考平台) project. It ensures all sub-agents follow consistent practices when working on this project.

## Purpose
- Maintain project consistency across all sub-agents
- Provide standardized approaches for common tasks
- Ensure proper documentation and code quality
- Guide proper development workflow

## Activation
This skill is automatically activated when working with the Gongwuyuan project. The system recognizes keywords like:
- "gongwuyuan"
- "gongwuyuan-app" 
- "公务员考试"
- "project"

## Usage Guidelines for Sub-Agents

When this skill is active, sub-agents should:

1. **Reference Documentation First**:
   - Always check requirements-document.md for feature specs
   - Consult api-contract.md for API interfaces
   - Follow web-technical-spec.md for implementation
   - Adhere to development-guidelines.md practices

2. **Follow Standard Workflow**:
   - Pull latest changes before starting work
   - Follow naming conventions and code standards
   - Update documentation when making changes
   - Test functionality thoroughly

3. **Maintain Quality Standards**:
   - Follow security best practices
   - Ensure API responses match contract specs
   - Write meaningful commit messages
   - Resolve conflicts carefully

## Helper Tools

A helper script is available to assist with common tasks:

```bash
# Check project status
/path/to/skills/gongwuyuan_project/scripts/project_helper.sh status

# Update project from main branch
/path/to/skills/gongwuyuan_project/scripts/project_helper.sh update

# Show available documentation
/path/to/skills/gongwuyuan_project/scripts/project_helper.sh docs

# Run project health checks
/path/to/skills/gongwuyuan_project/scripts/project_helper.sh check
```

## Constraints

Sub-agents using this skill must:
- Always prioritize existing documentation
- Maintain backward compatibility
- Follow the defined branching strategy
- Ensure all changes are properly tested
- Update relevant documentation when making changes