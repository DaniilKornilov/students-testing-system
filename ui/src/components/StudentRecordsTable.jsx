import React from 'react';
import { Box } from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';
import PropTypes from 'prop-types';

const columns = [
  {
    field: 'name',
    headerName: 'Имя студента',
    minWidth: 485,
    sortable: false,
  },
  {
    field: 'email',
    headerName: 'Email студента',
    minWidth: 485,
    sortable: false,
  },
  {
    field: 'group',
    headerName: 'Группа студента',
    minWidth: 485,
    sortable: false,
  },
];

function StudentRecordsTable(props) {
  const { students } = props;

  const rows = students ? students.map((s) => ({
    id: s.id,
    name: s.name,
    email: s.email,
    group: s.groupDto.name,
  })) : [];

  return (
    <Box
      sx={{
        mt: 2,
        '& .disabled': {
          backgroundColor: '#eeeeee',
        },
        '& .active': {
          backgroundColor: '#ffffff',
        },
      }}
    >
      <DataGrid
        id="studentsDataGrid"
        rowHeight={100}
        autoHeight
        pageSize={30}
        columns={columns}
        rows={rows}
        pagination
        disableColumnMenu
        disableSelectionOnClick
        disable
      />
    </Box>
  );
}

StudentRecordsTable.propTypes = {
  students: PropTypes.arrayOf(PropTypes.shape({
    id: PropTypes.number,
    name: PropTypes.string,
    email: PropTypes.string,
    groupDto: PropTypes.shape({ name: PropTypes.string }),
  })).isRequired,
};

export default StudentRecordsTable;
