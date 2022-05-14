import React from 'react';
import { Box } from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';
import PropTypes from 'prop-types';

const columns = [
  {
    field: 'name',
    headerName: 'Тест',
    minWidth: 375,
    sortable: false,
  },
  {
    field: 'availableFrom',
    headerName: 'Доступен с',
    minWidth: 375,
    sortable: false,
  },
  {
    field: 'availableTo',
    headerName: 'Доступен по',
    minWidth: 375,
    sortable: false,
  },
  {
    field: 'course',
    headerName: 'Курс',
    minWidth: 375,
    sortable: false,
  },
];

function TestRecordsTable(props) {
  const { tests } = props;

  const rows = tests ? tests.map((t) => ({
    id: t.id,
    name: t.name,
    availableFrom: t.availableFrom,
    availableTo: t.availableTo,
    course: t.courseDto.name,
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
        id="testsDataGrid"
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

TestRecordsTable.propTypes = {
  tests: PropTypes.arrayOf(PropTypes.shape({
    id: PropTypes.number,
    name: PropTypes.string,
    description: PropTypes.string,
    createdTimestamp: PropTypes.string,
    updatedTimestamp: PropTypes.string,
    timeLimit: PropTypes.string,
    availableFrom: PropTypes.string,
    availableTo: PropTypes.string,
    courseDto: PropTypes.shape({ name: PropTypes.string }),
  })).isRequired,
};

export default TestRecordsTable;
