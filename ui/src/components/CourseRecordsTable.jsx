import React from 'react';
import { Box } from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';
import PropTypes from 'prop-types';
import { useNavigate } from 'react-router-dom';

const columns = [
  {
    field: 'name',
    headerName: 'Курс',
    minWidth: 575,
    sortable: false,
  },
  {
    field: 'groups',
    headerName: 'Группы',
    minWidth: 575,
    sortable: false,
    renderCell: (params) => (
      <ul className="flex">
        {params.value.map((groupDto) => (
          <li key={groupDto.name}>{groupDto.name}</li>
        ))}
      </ul>
    ),
  },
];

function CourseRecordsTable(props) {
  const { courses } = props;
  const navigate = useNavigate();

  const rows = courses ? courses.map((c) => ({
    id: c.id,
    name: c.name,
    groups: c.groupDtos,
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
        id="coursesDataGrid"
        rowHeight={200}
        autoHeight
        pageSize={30}
        columns={columns}
        rows={rows}
        pagination
        disableColumnMenu
        disableSelectionOnClick
        disable
        onRowClick={(params) => {
          const course = params.row;
          const { name } = course;
          navigate('/tests', {
            state: {
              name,
            },
          });
        }}
      />
    </Box>
  );
}

CourseRecordsTable.propTypes = {
  courses: PropTypes.arrayOf(PropTypes.shape({
    id: PropTypes.number,
    name: PropTypes.string,
    groupDtos: PropTypes.arrayOf(PropTypes.shape({ name: PropTypes.string })),
  })).isRequired,
};

export default CourseRecordsTable;
